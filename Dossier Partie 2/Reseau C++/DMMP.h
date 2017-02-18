#ifndef DMMP_H
#define DMMP_H

#include <sstream>
#include "Config.h"
#include "Convert.h"
#include "Socket.h"
#include "SocketClient.h"

#define CONNECT 0
#define INPUT_DEVICES1 1
#define INPUT_DEVICES2 2
#define INPUT_DEVICES2B 3
#define GET_DELIVERY1 4
#define GET_DELIVERY2 5
#define GET_DELIVERY2_RESPONSE 6
#define GET_DELIVERY_END 7
#define DECONNECT 8


#define CONF_FILE "serveur_mouvements.ini"


#define CONNECT_ACCEPTED "1"
#define CONNECT_REFUSED "0"

#define DECONNECT_ACCEPTED "1"
#define DECONNECT_REFUSED "0"

#define SUPPLIER_FOUND "1"
#define SUPPLIER_UNFOUND "0"

#define DELIVERY_ACCEPTED "1"
#define NO_DELIVERY "0"
#define INVALID_DELIVERY "-1"

typedef struct
{
    int x;
    int y;
} Position;


typedef struct
{
    std::string name;
    std::string password;
} StructLogin;

typedef struct
{
    std::string supplier;
} StructInputDevices1;


typedef struct
{
    std::string ref;
    int qty;
} StructInputDevices2;

typedef struct
{
    Position pos;
    std::string error;
} StructInputDevices2B;

typedef struct
{
    std::string numeroCompany;
    std::string destinationArea;
} StructGetDelivery1;

typedef struct
{
    std::string ref;
    std::string error;
} StructResponseGetDelivery2;

class DMMP
{
    protected:
        std::string hostREDMMP;
        std::string portREDMMP;
        SocketClient *socket;
        Config conf;
        char separator;
    public:
        DMMP();
        DMMP(std::string conf_file);

        void setConfFile(std::string conf_file);

        std::string removeProtocol(std::string msg);
        int getProtocol(std::string msg);

        std::string composeConnect(StructLogin sl);
        StructLogin parseConnect(std::string msg);
        std::string composeDeconnect(StructLogin sl);
        StructLogin parseDeconnect(std::string msg);

        std::string composeInputDevices1(StructInputDevices1 sd);
        StructInputDevices1 parseInputDevices1(std::string msg);

        std::string composeInputDevices2(StructInputDevices2 sd);
        StructInputDevices2 parseInputDevices2(std::string msg);

        std::string composeInputDevices2B(StructInputDevices2B sd);
        StructInputDevices2B parseInputDevices2B(std::string msg);

        std::string composeGetDelivery1(StructGetDelivery1 sd);
        StructGetDelivery1 parseGetDelivery1(std::string msg);

        std::string composeGetDelivery2();
        std::string composeResponseGetDelivery2(StructResponseGetDelivery2 msg);
        StructResponseGetDelivery2 parseResponseGetDelivery2(std::string msg);

        std::string composeGetDeliveryEnd();
        std::string composeResponseGetDeliveryEnd();
        std::string parseResponseGetDeliveryEnd(std::string);

        std::string getValue(std::string content, int elem);
};

#endif
