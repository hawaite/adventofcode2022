#include <string>
#include<stdint.h>

class Command
{
    public:
        std::string nmemonic;
        int operand;
        uint8_t cycles;
        Command(std::string commandString);
};
