#include "SocketServeur.h"

using namespace std;

SocketServeur::SocketServeur(string host, int port, bool isIP) : Socket(host, port, isIP)
{

}

SocketServeur::~SocketServeur()
{

}

void SocketServeur::bind()
{
    if(::bind(socketHandle, (struct sockaddr*)&socketAddress, sizeof(struct sockaddr)) == -1)
        throw ErrnoException(errno, "Erreur sur le bind de la socket");
}

void SocketServeur::listen()
{
    if(::listen(socketHandle, SOMAXCONN) == -1)
        throw ErrnoException(errno, "Erreur sur le listen de la socket");
}

int SocketServeur::accept()
{
    int newHandle;
    unsigned int len = sizeof(struct sockaddr);

    if((newHandle = ::accept(socketHandle, (struct sockaddr*)&socketAddress, (socklen_t*)&len)) == -1)
        throw ErrnoException(errno, "Erreur sur l'accept de la socket");

    return newHandle;
}
