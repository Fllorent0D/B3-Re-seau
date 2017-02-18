#include "SocketClient.h"

using namespace std;

SocketClient::SocketClient(string host, int port, bool isIP) : Socket(host, port, isIP)
{

}

SocketClient::~SocketClient()
{

}

void SocketClient::connect()
{
    unsigned int len = sizeof(struct sockaddr);

    if(::connect(socketHandle, (struct sockaddr*)&socketAddress, len) == -1)
        throw ErrnoException(errno, "Erreur sur connect de la socket");
}
