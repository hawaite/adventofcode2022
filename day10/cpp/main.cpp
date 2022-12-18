#include <iostream>
#include <fstream>
#include <vector>
#include "sim.hpp"

int main(int argc, char** argv){
    Sim sim;

    std::ifstream fileStream("../input.txt");
    std::string line;

    if(fileStream.is_open()){
        while( std::getline(fileStream, line) ){
            sim.storeCommand(line);
        }
    }

    int currentTick = 1;
    int signalStrength = 0;

    while(sim.hasRemainingCommands()){
        // need to calculate signal strength DURING a tick, meaning before it has any effect.
        if(currentTick == 20 || currentTick == 60 || currentTick == 100 || currentTick == 140 || currentTick == 180 || currentTick == 220){
            signalStrength += (currentTick * sim.getX());
        }
        
        sim.tick();

        currentTick++;
    }

    std::cout << "signal strength value: " << signalStrength << std::endl;
    return 0;
}