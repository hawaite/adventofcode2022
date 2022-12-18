CC=g++
CCFLAGS=-Wall
BIN=main.bin
BIN_DIR=./bin
SOURCES=main.cpp sim.cpp command.cpp
run: $(BIN)
	$(BIN_DIR)/$(BIN)

main.bin:
	mkdir -p $(BIN_DIR)
	g++ $(CCFLAGS) $(SOURCES) -o $(BIN_DIR)/$(BIN)

clean:
	rm -rf $(BIN_DIR)