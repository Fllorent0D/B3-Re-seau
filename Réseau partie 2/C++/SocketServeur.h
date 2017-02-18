#ifndef SOCKET_SERVEUR_H
#define SOCKET_SERVEUR_H

#include <sys/socket.h>
#include <sys/types.h>

#include "Socket.h"
#include "exceptions/ErrnoException.h"

class SocketServeur : public Socket
{
    public:
        SocketServeur(std::string host, int port, bool isIP);
        ~SocketServeur();

        /* Methods */
        void bind();
        void listen();
        int accept();
};

#endif
