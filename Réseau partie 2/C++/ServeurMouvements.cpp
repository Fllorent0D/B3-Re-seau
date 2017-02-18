#include <iostream>
#include <signal.h>
#include <pthread.h>

#include "SocketServeur.h"
#include "SocketClient.h"
#include "exceptions/ErrnoException.h"
#include "Config.h"
#include "DMMP.h"
#include "AccessSerAp.h"

#define CONF_FILE "serveur_mouvements.ini"
#define LOGIN_FILE "login.csv"

#define MAX_CLIENTS 5


using namespace std;

Config conf(CONF_FILE);

string host = conf.getValue("HOST");
string port = conf.getValue("PORT");
string portUrgence = conf.getValue("PORT_URGENCE");

//string portUrgence = conf.getValue("PORT_URGENCE");
//const int MAX_CLIENTS = atoi(conf.getValue("MAX_CLIENTS").c_str());

pthread_mutex_t mutexCurrentIndex;
pthread_cond_t condCurrentIndex;
int currentIndex = -1;

pthread_mutex_t mutexLog;

/*pthread_cond_t condStop;
pthread_mutex_t mutexStop;*/

SocketServeur* socketConnected[MAX_CLIENTS];
int handleSocketConnected[MAX_CLIENTS];
pthread_t threadConnected[MAX_CLIENTS];

void* fctThread(void*);
//void* fctUrgence(void* p);
string responseConnect(StructLogin);
string responseDeconnect(StructLogin, string, string);
string responseInputDevices1(StructInputDevices1);
StructInputDevices2B responseInputDevices2(StructInputDevices2 sd);
string responseGetDelivery1(StructGetDelivery1 sd);
StructResponseGetDelivery2 responseGetDelivery2(string msg);

void handlerInt(int);


bool executeConnect(SocketServeur *sockCli);
bool executeDeconnect(SocketServeur *sockCli, string request);

SocketServeur socketEcoute(host, atoi(port.c_str()), 1);

AccessSerAp asp;

int stop = false;

string name, password;
DMMP dmmp(CONF_FILE);

int main()
{
    int i, ret;

    system("clear");

    cout << "Configuration serveur DMMP" << endl;
    cout << "--------------------------" << endl;
    cout << "Host: " << host << endl;
    cout << "Port: " << port << endl;
    cout << endl;

    struct sigaction hand;
    sigset_t mask;

    //asp.seedDB(); // Provisoire

    /* Initilisation */

    hand.sa_handler = handlerInt;
    sigemptyset(&hand.sa_mask);
    hand.sa_flags = 0;
    sigaction(SIGINT, &hand, NULL);
    sigfillset(&mask);
    sigdelset(&mask, SIGINT);
    pthread_sigmask(SIG_SETMASK, &mask, NULL);

    int socketHandle;

    pthread_mutex_init(&mutexCurrentIndex, NULL);
    pthread_cond_init(&condCurrentIndex, NULL);


    /*pthread_cond_init(&condStop, NULL);
    pthread_mutex_init(&mutexStop, NULL);*/

    /*pthread_t threadUrgence;

    pthread_create(&threadUrgence, NULL, (void* (*)(void*)) fctUrgence, NULL);
    pthread_detach(threadUrgence);*/

    for(i = 0; i < MAX_CLIENTS; i++)
    {
        socketConnected[i] = new SocketServeur(host, atoi(port.c_str()), 1);
        handleSocketConnected[i] = -1;
    }

    /* Creation du pool de threads */

    for(i = 0; i < MAX_CLIENTS; i++)
    {
        pthread_create(&threadConnected[i], NULL, fctThread, (void*)i);

        pthread_detach(threadConnected[i]);
    }

    /* Liaison de la socket */

    try
    {
        socketEcoute.bind();
    }
    catch(ErrnoException &e)
    {
        cout << e.getMessage() << endl;
        exit(1);
    }

    /* Mise sur écoute de la socket */

    do
    {
        try
        {
            socketEcoute.listen();
            socketHandle = socketEcoute.accept();
        }
        catch(ErrnoException &e)
        {
            if(e.getCode() == 4)
                break;

            cout << e.getCode() << ": " << e.getMessage() << endl;
            exit(1);
        }

        for(i = 0; i < MAX_CLIENTS && handleSocketConnected[i] != -1; i++);

        if(i == MAX_CLIENTS)
        {
            socketEcoute.sendString("Plus de connexion disponible");
        }
        else
        {
            pthread_mutex_lock(&mutexCurrentIndex);
            handleSocketConnected[i] = socketHandle;
            currentIndex = i;
            pthread_mutex_unlock(&mutexCurrentIndex);
            pthread_cond_signal(&condCurrentIndex);
        }
    } while(1);

    return 0;
}


void* fctThread(void* param)
{
    int iCli;
    int hSocketServ;
    int endDialog;
    string response;

    sigset_t mask;

    sigfillset(&mask);
    pthread_sigmask(SIG_SETMASK, &mask, NULL);

    while(1)
    {
        pthread_mutex_lock(&mutexCurrentIndex);
        while(currentIndex == -1)
            pthread_cond_wait(&condCurrentIndex, &mutexCurrentIndex);

        iCli = currentIndex;
        currentIndex = -1;

        cout << "Un client est pris en charge" << endl;

        socketConnected[iCli]->setSocketHandle(handleSocketConnected[iCli]);
        pthread_mutex_unlock(&mutexCurrentIndex);

        try
        {
            bool logged = false;
            string request;
            int protocol;

            do
            {
                logged = executeConnect(socketConnected[iCli]);
            } while(!logged);

            cout << "Client connecté" << endl;

            bool terminated = 0;


            do
            {
                request = socketConnected[iCli]->receiveString();

                protocol = dmmp.getProtocol(request);

                cout << "Protocol: " << protocol << endl;

                switch(protocol) // don't remove brackets
                {
                    case DECONNECT:
                    {
                        executeDeconnect(socketConnected[iCli], request);
                        terminated = true;
                        break;
                    }
                    case INPUT_DEVICES1:
                    case INPUT_DEVICES2:
                    case GET_DELIVERY1:
                    case GET_DELIVERY2:
                    case GET_DELIVERY_END:
                    {
                        asp.sendRequest(request);
                        socketConnected[iCli]->sendString(asp.receiveResponse());
                        break;
                    }
                    default:
                        terminated = true;
                }
            }while(!terminated && !stop);

            if(stop)
                cout << "Serveur coupé" << endl;
        }
        catch(ErrnoException &e)
        {
            if(e.getCode() != 0)
                cout << e.getCode() << " " << e.getMessage() << endl;
        }

        cout << "Un Client est parti" << endl;

        pthread_mutex_lock(&mutexCurrentIndex);
        handleSocketConnected[iCli] = -1;
        pthread_mutex_unlock(&mutexCurrentIndex);
    }

    cout << "Fin du thread" << endl;

    return NULL;
}

bool executeConnect(SocketServeur *sockCli)
{
    bool logged = false;

    string request = sockCli->receiveString();
    int protocole = dmmp.getProtocol(request);

    if(protocole != CONNECT)
        return false;

    name = dmmp.getValue(request,1);
    password = dmmp.getValue(request,2);

    asp.sendRequest(request);

    string response = asp.receiveResponse();

    string content = asp.getContent(response);

    if(content == "1")
        logged = true;

    sockCli->sendString(response);

    return logged;
}

bool executeDeconnect(SocketServeur *sockCli, string request)
{
    bool logged = false;
    string response;
    int protocol = dmmp.getProtocol(request);
    string content = dmmp.removeProtocol(request);

    StructLogin sl = dmmp.parseDeconnect(content);

    if(name.compare(sl.name) == 0 && password.compare(sl.password) == 0)
    {
        content = DECONNECT_ACCEPTED;
        asp.sendRequest(request);
    }
    else
        content = DECONNECT_REFUSED;

    response = Convert::intToString(protocol) + "#" + content;

    sockCli->sendString(response);

    return logged;
}

void handlerInt(int)
{
    cout << "Fermeture du serveur ..." << endl;

    for(int i = 0 ; i < MAX_CLIENTS ; i++)
    {
        socketConnected[i]->close();
        int ret = pthread_kill(threadConnected[i], SIGINT);

        if(ret == 0)
            cout << "Fin du thread numero " << i << endl;
        else
            cout << "Echec de la fin du thread numero " << i << endl;
    }

    socketEcoute.close();
}

/*void* fctUrgence(void* p)
{
    SocketServeur SSocket(host, atoi(portUrgence.c_str()), 1);
    SocketServeur CSocket(host, atoi(portUrgence.c_str()), 1);

    sigset_t mask;

    sigfillset(&mask);
    pthread_sigmask(SIG_SETMASK, &mask, NULL);

    cout << "Configuration serveur urgence" << endl;
    cout << "-----------------------------" << endl;
    cout << "Host: " << host << endl;
    cout << "Port: " << portUrgence << endl;
    cout << endl;

    try
    {
        SSocket.bind();
    }
    catch(ErrnoException &e)
    {
        cout << e.getMessage() << endl;
        exit(1);
    }

    while(1)
    {
        try
        {
            SSocket.listen();
            cout << "Thread urgence en attente d'une connexion" << endl;
            int socketHandle = SSocket.accept();
            cout << "Connexion urgence établie" << endl;

            CSocket.setSocketHandle(socketHandle);

            string request = CSocket.receiveString();

            cout << "Demande de stopage du serveur...";

            stop = true;

            CSocket.close();
        }
        catch(ErrnoException &e)
        {
            if(e.getCode() != 0)
                cout << e.getCode() << " " << e.getMessage() << endl;

            CSocket.close();
        }
    }

    return NULL;
}*/













