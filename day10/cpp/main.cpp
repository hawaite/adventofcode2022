#include <iostream>
#include <fstream>
#include <vector>
#include "sim.hpp"

char calculatePixel(int pixelLocation, int spriteCenter){
    if(((spriteCenter -1) <= pixelLocation) && ((spriteCenter + 1) >= pixelLocation))
        return '#';
    return '.';
}

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
    int spriteCentre = sim.getX();
    int pixelToDraw = ((currentTick -1) % 40);
    while(sim.hasRemainingCommands()){
        // before
        if(currentTick == 20 || currentTick == 60 || currentTick == 100 || currentTick == 140 || currentTick == 180 || currentTick == 220){
            signalStrength += (currentTick * sim.getX());
        }

        // during
        //update sprite center
        spriteCentre = sim.getX();
        pixelToDraw = ((currentTick -1) % 40);

        std::cout << calculatePixel(pixelToDraw, spriteCentre);
        if(pixelToDraw == 39)
            std::cout << std::endl;

        // after
        sim.tick();

        currentTick++;
    }

    std::cout << std::endl << "signal strength value: " << signalStrength << std::endl;
    return 0;
}