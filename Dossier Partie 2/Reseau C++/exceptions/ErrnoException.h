#ifndef ERRNOEXCEPTION_H
#define ERRNOEXCEPTION_H

#include <iostream>
#include <string.h>

class ErrnoException
{
    protected:
        int errno;
        std::string message;

    public:
        ErrnoException(int e, std::string m);
        int getCode();
        std::string getMessage();
};

#endif
