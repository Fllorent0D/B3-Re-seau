#include "Convert.h"

using namespace std;

string Convert::intToString(int i)
{
    string number;

    if(i == 0)
        number = "0";
    else
    {
        while(i > 0)
        {
            int digit = i%10;

            char digitToChar = digit + 48;

            i = i / 10;

            number = digitToChar + number;
        }
    }

    return number;
}

