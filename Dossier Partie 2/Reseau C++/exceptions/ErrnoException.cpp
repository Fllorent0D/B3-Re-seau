#include "ErrnoException.h"

using namespace std;

ErrnoException::ErrnoException(int e, string m)
{
    errno = e;
    message = m;
}

int ErrnoException::getCode()
{
    return errno;
}

string ErrnoException::getMessage()
{
    return message;
}
