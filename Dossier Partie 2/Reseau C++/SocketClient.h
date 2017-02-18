#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include "Socket.h"
#include "exceptions/ErrnoException.h"

class SocketClient : public Socket
{
    public:
        SocketClient(std::string host, int port, bool isIP);
        ~SocketClient();

        /* Method */
        void connect();
};

#endif
