#include "AccessSerAp.h"

using namespace std;


AccessSerAp::AccessSerAp()
{
    conf.setFilename(CONF_FILE);
    host = conf.getValue("HOST_DMMP");
    port = conf.getValue("PORT_DMMP");

    cout << "Configuration de REDMMP" << endl;
    cout << "-----------------------" << endl;
    cout << "Host: " << host << endl;
    cout << "Port: " << port << endl;
    cout << endl;

    sock = new SocketClient(host, atoi(port.c_str()), 1);

    try
    {
       sock->connect();
    }
    catch(ErrnoException &e)
    {
       cout << e.getMessage() << endl;
       exit(1);
    }

}

void AccessSerAp::sendRequest(string content)
{
    sock->sendString(content);
}

string AccessSerAp::receiveResponse()
{
    string response = sock->receiveString();

    return response;
}

string AccessSerAp::getContent(string response)
{
    int pos = response.find("#");

    return response.substr(pos+1);
}

int AccessSerAp::getProtocol(string response)
{
    int pos = response.find("#");

    return atoi(response.substr(0, pos).c_str());
}

string AccessSerAp::getValue(string content, int elem)
{
    string buff = content;
    int pos;

    for(int i = 0 ; i < elem ; i++)
    {
        pos = buff.find("#");

        if(pos == -1)
        {
            buff = buff.substr(0, pos);
        }
        else
        {
            buff = buff.substr(pos+1);
        }
    }

    pos = buff.find("#");


    if(pos != -1)
        buff = buff.substr(0, pos);

    return buff;
}
