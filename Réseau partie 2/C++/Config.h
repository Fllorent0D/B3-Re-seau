#ifndef CONFIG_H
#define CONFIG_H

#include <stdlib.h>
#include <fstream>
#include <iostream>
#include <string.h>
#include <string.h>

class Config
{
    protected:
        std::ifstream ifs;
        std::string filename;
    public:
        Config();
        Config(std::string f);
        Config(const Config& c);
        ~Config();
        std::string getValue(std::string value);
        std::string getFilename() const;
        void setFilename(const std::string f);
        Config& operator=(const Config& c);
};

#endif
