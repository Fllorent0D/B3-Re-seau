#include "DMMP.h"

using namespace std;

DMMP::DMMP()
{

}

DMMP::DMMP(string conf_file)
{
    conf.setFilename(conf_file);
    separator = *(conf.getValue("SEP_TRAME").c_str());
}

void DMMP::setConfFile(string conf_file)
{
   conf.setFilename(conf_file);
   separator = *(conf.getValue("SEP_TRAME").c_str());
}


string DMMP::removeProtocol(string msg)
{
    int pos;

    pos = msg.find("#");

    msg = msg.substr(pos+1);

    return msg;
}


int DMMP::getProtocol(string msg)
{
    istringstream iss(msg);
    string method;

    getline(iss, method, separator);

    return atoi(method.c_str());
}


string DMMP::composeConnect(StructLogin sl)
{
    return Convert::intToString(CONNECT) + separator + sl.name + separator + sl.password;
}


StructLogin DMMP::parseConnect(string msg)
{
    msg = removeProtocol(msg);

    istringstream iss(msg);
    StructLogin sl;
    string buff;

    getline(iss, buff, separator);
    sl.name = buff;

    getline(iss, buff, separator);
    sl.password = buff;

    return sl;
}

string DMMP::composeDeconnect(StructLogin sl)
{
    return Convert::intToString(DECONNECT) + separator + sl.name + separator + sl.password;
}

StructLogin DMMP::parseDeconnect(string msg)
{
    return parseConnect(msg); //Methodes identiques
}

string DMMP::composeInputDevices1(StructInputDevices1 sd)
{
    return Convert::intToString(INPUT_DEVICES1) + separator + sd.supplier;
}

StructInputDevices1 DMMP::parseInputDevices1(string msg)
{
    msg = removeProtocol(msg);

    istringstream iss(msg);
    StructInputDevices1 sd;
    string buff;

    getline(iss, buff, separator);
    sd.supplier = buff;

    return sd;
}

std::string DMMP::composeInputDevices2(StructInputDevices2 sd)
{
    return Convert::intToString(INPUT_DEVICES2) + separator + sd.ref + separator + Convert::intToString(sd.qty);
}

StructInputDevices2 DMMP::parseInputDevices2(std::string msg)
{
    msg = removeProtocol(msg);

    istringstream iss(msg);
    StructInputDevices2 sd;
    string buff;

    getline(iss, buff, separator);
    sd.ref = buff;

    getline(iss, buff, separator);
    sd.qty = atoi(buff.c_str());

    return sd;
}


string DMMP::composeInputDevices2B(StructInputDevices2B sd)
{
    return Convert::intToString(INPUT_DEVICES2B) + separator + Convert::intToString(sd.pos.x) + separator + Convert::intToString(sd.pos.y) + separator + sd.error;
}


StructInputDevices2B DMMP::parseInputDevices2B(string msg)
{
    istringstream iss(msg);
    StructInputDevices2B sd;
    string buff;

    msg = removeProtocol(msg);

    getline(iss, buff, separator);
    sd.pos.x = atoi(buff.c_str());

    getline(iss, buff, separator);
    sd.pos.y = atoi(buff.c_str());

    getline(iss, buff, separator);
    sd.error = buff;

    return sd;
}

string DMMP::composeGetDelivery1(StructGetDelivery1 sd)
{
    return Convert::intToString(GET_DELIVERY1) + separator + sd.numeroCompany + separator + sd.destinationArea;
}

StructGetDelivery1 DMMP::parseGetDelivery1(string msg)
{
    msg = removeProtocol(msg);

    istringstream iss(msg);
    StructGetDelivery1 sd;
    string buff;

    getline(iss, buff, separator);
    sd.numeroCompany = buff;

    getline(iss, buff, separator);
    sd.destinationArea = buff;

    return sd;
}

string DMMP::composeGetDelivery2()
{
    return Convert::intToString(GET_DELIVERY2) + separator;
}

string DMMP::composeResponseGetDelivery2(StructResponseGetDelivery2 msg)
{
    return Convert::intToString(GET_DELIVERY2_RESPONSE) + separator + msg.ref + separator + msg.error;
}

StructResponseGetDelivery2 DMMP::parseResponseGetDelivery2(string msg)
{
    msg = removeProtocol(msg);

    istringstream iss(msg);
    StructResponseGetDelivery2 sd;
    string buff;

    getline(iss, buff, separator);
    sd.ref = buff;

    getline(iss, buff, separator);
    sd.error = buff;

    return sd;
}

string DMMP::composeGetDeliveryEnd()
{
    return Convert::intToString(GET_DELIVERY_END) + separator;
}

string DMMP::getValue(string content, int elem)
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



