#ifndef ACCESSSERAP_H
#define ACCESSSERAP_H

#include "Convert.h"
#include "SocketClient.h"
#include "Config.h"

#define CONF_FILE "serveur_mouvements.ini"

class AccessSerAp
{
protected:
    std::string host;
    std::string port;
    SocketClient* sock;
    Config conf;
public:
    AccessSerAp();
    void sendRequest(std::string msg);
    std::string receiveResponse();
    std::string getContent(std::string response);
    int getProtocol(std::string response);
    std::string getValue(std::string content, int elem);
};

#endif
