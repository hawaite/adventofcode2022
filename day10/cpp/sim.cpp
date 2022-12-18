#include "sim.hpp"
#include <string>
#include <iostream>

void Sim::tick(){
    Command* currentCommand = commandVector[programCounter];

    // starting new command requiring more than 1 cycle
    if(midCommand == false && currentCommand->cycles > 1){
        // set up number of cycles to spin and set flag that we are mid-way through a command
        remainingTicksOnCurrentCommand = currentCommand->cycles;
        midCommand = true;
    }

    // remaining ticks on current command > 0, decrement it. if not now zero return.
    if(remainingTicksOnCurrentCommand > 0){
        remainingTicksOnCurrentCommand -=1;
    }

    if(remainingTicksOnCurrentCommand != 0){
        return;
    }

    if(currentCommand->nmemonic == "addx"){
        xRegister += currentCommand->operand;
    }
    else if(currentCommand->nmemonic == "noop"){
    }

    midCommand = false;
    remainingTicksOnCurrentCommand = 0;
    programCounter +=1;
}

bool Sim::hasRemainingCommands(){
    return programCounter < ( Sim::commandVector.size());
}

int Sim::getX(){
    return Sim::xRegister;
}

void Sim::storeCommand(std::string commandLine){
    commandVector.push_back(new Command(commandLine));
}
