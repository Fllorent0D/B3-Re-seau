#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <iostream>
#include <signal.h>

#include "SocketClient.h"
#include "SocketServeur.h"
#include "exceptions/ErrnoException.h"
#include "Config.h"
#include "DMMP.h"

#define CONF_FILE "serveur_mouvements.ini"

using namespace std;

typedef struct // Affiche un message une seule fois
{
    string title;
    string message;
} StructFlash;

void login();
int deconnect();
void inputDevices1();
void inputDevices2();
void inputDevices3();
void getDelivery1();
void getDelivery2();
void getDeliveryEnd();
int displayMenu();
void displayFlash();

void sendString(SocketClient* sock, string message);
string receiveString(SocketClient* sock);

void handlerInt(int);
void* threadUrgence(void* p);

Config conf(CONF_FILE);
string host = conf.getValue("HOST");
string port = conf.getValue("PORT");
string portUrgenceInterne = conf.getValue("PORT_URGENCE_INTERNE");

string nameUser;

bool stop = false;

StructFlash flash;
DMMP dmmp(CONF_FILE);

SocketClient sock(host, atoi(port.c_str()), 1);

int main()
{
    string msg;
    int choice;

    struct sigaction hand;
    sigset_t mask;

    /* Initilisation */

    /*hand.sa_handler = handlerInt;
    sigemptyset(&hand.sa_mask);
    hand.sa_flags = 0;
    sigaction(SIGINT, &hand, NULL);

    sigfillset(&mask);
    sigdelset(&mask, SIGINT);
    pthread_sigmask(SIG_SETMASK, &mask, NULL);*/

    try
    {
        sock.connect();

        login();

        system("clear");

        flash.title = "Message";
        flash.message = "Vous etes connecte au serveur";

        int endDialog = 0;

        do
        {
            choice = displayMenu();

            switch(choice)
            {
                case 1:
                    inputDevices1();
                break;
                case 2:
                    getDelivery1();
                break;
                case 10:
                    if(deconnect())
                    {
                        system("clear");
                        cout << "Deconnexion du serveur ..." << endl;
                        sock.close();
                        cout << "Fin du programme" << endl;
                        exit(1);
                    }
                break;
            }

        } while(!endDialog);

        sock.close();
    }
    catch(ErrnoException &e)
    {
        cout << e.getCode() << " " << e.getMessage() << endl;
        exit(1);
    }

    return 0;
}

void login()
{
    StructLogin sl;
    string msg;
    int logged = 0;
    string error = "";

    do
    {
        system("clear");

        cout << "Connexion au serveur" << endl;
        cout << "--------------------" << endl;

        if(error != "")
        {
            cout << "Erreur: " << error << endl << endl;
            error = "";
        }

        cout << "Nom: ";
        cin >> sl.name;

        cout << "Password: ";
        cin >> sl.password;

        cout << "Tentative de connexion au serveur ..." << endl;

        msg = dmmp.composeConnect(sl);

        sendString(&sock, msg);
        msg = receiveString(&sock);

        msg = dmmp.removeProtocol(msg);

        if(msg == CONNECT_ACCEPTED)
        {
            logged = 1;
            nameUser = sl.name;
        }
        else
            error = "Nom ou password invalide";
    }while(!logged);
}

int deconnect()
{
    StructLogin sl;
    string msg;
    int unlogged = 0;

    system("clear");

    cout << "Deconnexion du serveur" << endl;
    cout << "----------------------" << endl;

    sl.name = nameUser;

    cout << "Password: ";
    cin >> sl.password;

    msg = dmmp.composeDeconnect(sl);

    cout << msg << endl;

    sendString(&sock, msg);
    msg = receiveString(&sock);

    msg = dmmp.removeProtocol(msg);

    if(msg == DECONNECT_ACCEPTED)
        unlogged = 1;
    else
    {
        flash.title = "Erreur";
        flash.message = "Password invalide";
    }

    return unlogged;
}

void inputDevices1()
{
    StructInputDevices1 sd1;
    string msg;
    string supplier;

    system("clear");

    cout << "Reception de marchandises" << endl;
    cout << "-------------------------" << endl;

    cout << "Nom du fournisseur: ";
    cin >>  sd1.supplier;

    msg = dmmp.composeInputDevices1(sd1);

    sendString(&sock, msg);
    msg = receiveString(&sock);

    msg = dmmp.removeProtocol(msg);

    if(msg == SUPPLIER_FOUND)
    {
        inputDevices2();
    }
    else
    {
        flash.title = "Erreur";
        flash.message = "Fournisseur inconnu";
    }
}

void inputDevices2()
{
    string msg;
    int choice;
    StructInputDevices2 sd2;

    system("clear");

    cout << "Enumeration des marchandises" << endl;

    do
    {
        cout << "----------------------------" << endl;

        cout << "Identifiant: ";
        cin >> sd2.ref;

        cout << "Quantité: ";
        cin >> sd2.qty;

        msg = dmmp.composeInputDevices2(sd2);

        sendString(&sock, msg);
        msg = receiveString(&sock);

        msg = dmmp.removeProtocol(msg);

        StructInputDevices2B sd2B = dmmp.parseInputDevices2B(msg);

        cout << "----------------------------" << endl;

        if(sd2B.error == "")
            cout << "Message: Emplacement attribué : X->" << sd2B.pos.x << " Y->" << sd2B.pos.y << endl;
        else
            cout << "Erreur: " << sd2B.error << endl;

        cout << "----------------------------" << endl;

        do
        {
            cout << "Ajouter une nouvelle marchandise (1/0): ";
            cin >> choice;
        } while(choice != 1 && choice != 0);

    } while(choice == 1);
}

void getDelivery1()
{
    StructGetDelivery1 sd1;
    string msg;

    system("clear");
    cout << "Parametres de livraison" << endl;
    cout << "-----------------------" << endl;

    cout << "Numero d'entreprise du véhicule: ";
    cin >> sd1.numeroCompany;

    cout << "Zone de destination: ";
    cin >> sd1.destinationArea;

    msg = dmmp.composeGetDelivery1(sd1);

    sendString(&sock, msg);
    msg = receiveString(&sock);

    msg = dmmp.removeProtocol(msg);

    string vehiculeFounded = dmmp.getValue(msg, 1);
    string zoneFounded = dmmp.getValue(msg, 2);

    if(vehiculeFounded == "1" && zoneFounded == "1")
        getDelivery2();
    else if(vehiculeFounded == "0")
    {
        flash.title = "Message";
        //flash.message = "Pas de véhicule disponible pour cette societe";
        flash.message = msg + " " + vehiculeFounded + " " + zoneFounded;
    }
    else if(zoneFounded == "0")
    {
        flash.title = "Message";
        flash.message = "Pas de livraison a effectuer pour cette zone";
    }
    else
    {
        flash.title = "Erreur";
        flash.message = "Commande non valide";
    }
}

void getDelivery2()
{
    int restart = 1;
    string msg;

    system("clear");

    cout << "Chargement des produits" << endl;

    do
    {
        cout << "-----------------------" << endl;

        msg = dmmp.composeGetDelivery2();

        sendString(&sock, msg);
        msg = receiveString(&sock);

        msg = dmmp.removeProtocol(msg);

        string reference = dmmp.getValue(msg, 1);
        string error = dmmp.getValue(msg, 2);

        if(error != "")
        {
            flash.title = "Erreur";
            flash.message = error;
            restart = 0;
        }
        else
        {
            cout << "Reference: " << msg << endl;

            cout << "-----------------------" << endl;
            do
            {
                cout << "Charger un produit (1/0) :";
                cin >> restart;
            }while(restart != 0 && restart != 1);
        }

    } while(restart);

    getDeliveryEnd();
}

void getDeliveryEnd()
{
    string msg;

    system("clear");

    cout << "Signalement du depart du vehicule" << endl;
    cout << "---------------------------------" << endl;

    cout << "Appuyer sur une touche pour signaler le depart" << endl;
    /*cin.ignore(1024, '\n');
    cin.get();*/

    msg = dmmp.composeGetDeliveryEnd();

    sendString(&sock, msg);
    msg = receiveString(&sock);

    msg = dmmp.removeProtocol(msg);

    if(msg == "1")
    {
        flash.title = "Message";
        flash.message = "Notification enregistree";
    }
    else
    {
        flash.title = "Erreur";
        flash.message = "Plus rien pour cette zone";
    }
}


int displayMenu()
{
    int choice = 0;

    system("clear");

    cout << "Menu" << endl;
    cout << "----" << endl;

    displayFlash();

    cout << "1. Arrive d'un fournisseur" << endl;
    cout << "2. Chargement" << endl;
    cout << "10. Deconnexion" << endl;

    cout << endl;

    do
    {
        cout << "Votre choix : ";
        cin >> choice;
    } while((choice < 1 || choice > 2) && choice != 10);

    return choice;
}

void displayFlash()
{
    if(flash.title != "" && flash.message != "")
    {
        cout << flash.title << ": " << flash.message << endl << endl;
        flash.title = "";
        flash.message = "";
    }
}

void handlerInt(int)
{
    cout << "Fermeture du client ..." << endl;

    sock.close();
}

void sendString(SocketClient* sock, string message)
{
    if(stop)
    {
        cout << "Arrêt du serveur" << endl;
        exit(0);
    }

    return sock->sendString(message);
}

string receiveString(SocketClient* sock)
{
    if(stop)
    {
        cout << "Arrêt du serveur" << endl;
        exit(0);
    }

    return sock->receiveString();
}






