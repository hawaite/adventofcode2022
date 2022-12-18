#include "command.hpp"

Command::Command(std::string commandString){
    nmemonic = commandString.substr(0,4);
    if(nmemonic == "addx"){
        std::string operandString = commandString.substr(5);
        operand = std::stoi( operandString );
        cycles = 2;
    }
    if(nmemonic == "noop"){
        cycles = 1;
    }
}