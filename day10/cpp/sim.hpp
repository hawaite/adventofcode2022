#include <string>
#include <vector>
#include <stdint.h>
#include "command.hpp"

class Sim{
    public:
        void tick();
        int getX();
        void storeCommand(std::string commandLine);
        bool hasRemainingCommands();
        uint8_t remainingTicksOnCurrentCommand = 0;
    private:
        bool midCommand = false;
        int32_t xRegister = 1;
        uint32_t programCounter = 0;
        std::vector<Command*> commandVector = {};
};