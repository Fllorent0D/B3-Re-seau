#ifndef ACCESSSERAP_H
#define ACCESSSERP_H


#define CONF_FILE "serveur_mouvements.ini"


#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <fstream>
#include <iostream>
#include <string.h>
#include "Convert.h"
#include "SocketClient.h"
#include "DMMP.h"

#define MAX_WAREHOUSE_X_LENGTH 10
#define MAX_WAREHOUSE_Y_LENGTH 10


typedef struct
{
    std::string name;
}
StructSupplier;

typedef struct
{
    std::string ref;
    int qty;
    int x;
    int y;
}
StructWarehouse;

class AccessSerAp
{
    protected:
        std::string warehouseFile = "warehouse.data";
        std::string suppliersFile = "suppliers.data";
        std::string ordersFile = "orders.data";
        SocketClient *sock;
    public:
        AccessSerAp(SocketClient *sock);
        std::string connect(StructLogin sl);

        void seedDB();

        int getSupplier(std::string f);

        int insertGoods(StructWarehouse*, std::string*);

        int getX(char* ra);
        int getY(char* ra);

        std::string getValue(std::string buff, int elem, std::string sep);
};

#endif
