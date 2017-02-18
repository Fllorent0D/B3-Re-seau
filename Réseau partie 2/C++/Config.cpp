#include "Config.h"

using namespace std;

Config::Config()
{

}

Config::Config(string f)
{
    filename = f;
    ifs.open(filename.c_str());
}

Config::Config(const Config& c)
{
    setFilename(c.getFilename());
}

Config::~Config()
{
    if(ifs.is_open())
        ifs.close();
}

string Config::getValue(string value)
{
    string buff;

    if(!(ifs.is_open()))
        ifs.open(filename.c_str());

    ifs.clear();
    ifs.seekg(0, ios::beg);

    while(getline(ifs, buff))
    {
        char* token = strtok((char*) buff.c_str(), "=");

        if(strcmp(token, value.c_str()) == 0)
        {
            return strtok(NULL, "=");
        }
    }

    return NULL;
}

string Config::getFilename() const
{
    return filename;
}

void Config::setFilename(const string f)
{
    filename = f;
}


Config& Config::operator=(const Config& c)
{
    setFilename(c.getFilename());

    return *this;
}
