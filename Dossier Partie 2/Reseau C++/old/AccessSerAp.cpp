#include "AccessSerAp.h"

using namespace std;


AccessSerAp::AccessSerAp(SocketClient *s)
{
    cout << "Lancement de AccessSerAp" << endl;
    sock = s;
    dmmp.setConfFile(CONF_FILE);
}

string AccessSerAp::connect(StructLogin sl)
{
    string request = dmmp.connect(sl);

    sock->sendString(request);

    string response = sock->receiveString();


}

void AccessSerAp::seedDB()
{
    ofstream ofs;

    ofs.open(suppliersFile.c_str(), ofstream::out);

    StructSupplier ss;

    ss.name = "Siemens";
    ofs << ss.name << '\n';

    ss.name = "BOSH";
    ofs << ss.name << '\n';

    ss.name = "Karcher";
    ofs << ss.name << '\n';

    ss.name = "Miele";
    ofs << ss.name << '\n';

    ss.name = "Krups";
    ofs << ss.name << '\n';

    ofs.close();

    ofs.open(warehouseFile.c_str(), ofstream::out);

    StructWarehouse sw;

    sw.ref = "siemens-tv-001";
    sw.qty = 10;
    sw.y = 1;
    sw.x = 1;
    ofs << sw.ref << '#' << sw.qty << '#' << sw.y << '#' << sw.x << '\n';

    sw.ref = "bosh-tv-001";
    sw.qty = 15;
    sw.y = 1;
    sw.x = 2;
    ofs << sw.ref << '#' << sw.qty << '#' << sw.y << '#' << sw.x << '\n';

    sw.ref = "miele-tv-001";
    sw.qty = 8;
    sw.y = 1;
    sw.x = 3;
    ofs << sw.ref << '#' << sw.qty << '#' << sw.y << '#' << sw.x << '\n';

    ofs.close();

    ofs.open(ordersFile.c_str(), ofstream::out);

    ofs << "simens-tv-001" << '#' << "2" << '#' << "1" << '\n';
    ofs << "miele-tv-001" << '#' << "3" << '#' << "1" << '\n';
    ofs << "miele-tv-001" << '#' << "2" << '#' << "1" << '\n';
}

/*
* Role: vérification de l'existence d'un fournisseur
* Valeur retournée: 0 ou 1
*/
int AccessSerAp::getSupplier(string s)
{
    ifstream ifs;
    string buff;

    ifs.open(suppliersFile.c_str(), ifstream::out);

    getline(ifs, buff, '\n');

    while(!ifs.eof())
    {
        if(strcasecmp(buff.c_str(), s.c_str()) == 0)
        {
            ifs.close();
            return 1;
        }

        getline(ifs, buff, '\n');
    }

    ifs.close();

    return 0;
}

int AccessSerAp::insertGoods(StructWarehouse *s, string *error)
{
    int warehouse[MAX_WAREHOUSE_X_LENGTH][MAX_WAREHOUSE_Y_LENGTH] = {};
    string buff;
    fstream fs;
    int x, y;
    int i, j;
    int free = 0;

    s->x = -1;
    s->y = -1;

    if(s->qty < 1)
    {
        *error = "La quantité doit etre superieur à 0";
        fs.close();
        return 0;
    }

    fs.open(warehouseFile.c_str(), fstream::in | fstream::out);

    getline(fs, buff, '\n');

    while(!fs.eof())
    {
        x = atoi(getValue(buff, 3, "#").c_str());
        y = atoi(getValue(buff, 4, "#").c_str());

        warehouse[x][y] = 1;

        getline(fs, buff, '\n');
    }

    cout << "Etat de l'entrepot" << endl;
    cout << "------------------" << endl;

    for(i = 1 ; i <= MAX_WAREHOUSE_X_LENGTH ; i++)
    {
        for(j = 1 ; j <= MAX_WAREHOUSE_Y_LENGTH ; j++)
        {
            if(warehouse[i][j] == 0)
            {
                s->x = i;
                s->y = j;
                free = 1;
                break;
            }
        }
    }


    for(i = 1 ; i <= MAX_WAREHOUSE_X_LENGTH ; i++)
    {
        for(j = 1 ; j <= MAX_WAREHOUSE_Y_LENGTH ; j++)
        {
            if(warehouse[i][j] == 0)
            {
                s->x = i;
                s->y = j;
                free = 1;
                break;
            }
        }
    }

    if(free != 1)
    {
        *error = "Il ne reste plus de place dans l'entrepot";
        fs.close();
        return 0;
    }

    fs.close();// Erreur si le fichier n'est pas d'abord refermé
    fs.open(warehouseFile.c_str(), fstream::in | fstream::out);

    fs.seekg(0, ios::end);

    fs << s->ref << "#" << s->x << "#" << s->y << "#" << s->qty << "\n";

    fs.close();

    return 1;
}

/*
* Role: recherche de la position d'entreposage pour un appareil de référence donnée
* Valeur retournée: les positions demandées (-1 si pas trouvé)
*/
int AccessSerAp::getX(char* ra)
{
    ifstream ifs;
    string buff;
    string value;
    string ref;
    int x;
    int pos;

    ifs.open(warehouseFile.c_str(), ifstream::out);

    getline(ifs, buff, '\n');

    while(!ifs.eof())
    {
        pos = buff.find("#");
        value = buff.substr(pos+1);

        ref = buff.substr(0, pos);

        if(strcasecmp(ref.c_str(), ra) == 0)
        {
            pos = value.find("#");
            value = value.substr(pos+1);

            pos = value.find("#");

            x = atoi(value.substr(0, pos).c_str());

            ifs.close();

            return x;
        }

        getline(ifs, buff, '\n');
    }

    ifs.close();

    return -1;
}

/*
* Role: vérification de l'existence d'une livraison pour une zone donnée
* Valeur retournée: 0 ou 1
*/
int AccessSerAp::getY(char* ra)
{
    ifstream ifs;
    string buff;
    string value;
    string ref;
    int x;
    int pos;

    ifs.open(warehouseFile.c_str(), ifstream::out);

    getline(ifs, buff, '\n');

    while(!ifs.eof())
    {
        ref = getValue(buff, 1, "#");

        if(strcasecmp(ref.c_str(), ra) == 0)
        {
            x = atoi(getValue(buff, 3, "#").c_str());

            ifs.close();

            return x;
        }

        getline(ifs, buff, '\n');
    }

    ifs.close();

    return -1;
}

string AccessSerAp::getValue(string buff, int elem, string sep)
{
    string value;
    int pos;

    for(int i = 0 ; i < elem ; i++)
    {
        pos = buff.find(sep);
        value = buff.substr(0, pos);
        buff = buff.substr(pos+1);
    }

    return value;
}



