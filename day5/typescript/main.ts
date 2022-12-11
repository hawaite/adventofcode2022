import * as fs from "fs";
import * as lodash from "lodash";

const stateColumnWidth = 4;

class Command {
    private commandRegex: RegExp = new RegExp(
        /move (?<count>\d+) from (?<source>\d+) to (?<destination>\d+)/
    );

    count: number;
    source: number;
    destination: number;

    constructor(textCommand: string) {
        var results = textCommand.match(this.commandRegex);
        if (results !== null && results.groups !== undefined) {
            this.count = parseInt(results.groups["count"]);
            this.source = parseInt(results.groups["source"]);
            this.destination = parseInt(results.groups["destination"]);
        } else {
            throw Error("Could not parse command from " + textCommand);
        }
    }
}

class State {
    private columnStateMap: Map<number, string[]>;

    constructor(initialStateRows: string[]) {
        this.columnStateMap = new Map();

        initialStateRows.reverse().forEach((value) => {
            let parsedRow = lodash
                .chunk(value, stateColumnWidth)
                .map((item) => item[1]);
            parsedRow.forEach((item, ix) => {
                this.pushToColumn(ix + 1, item);
            });
        });
    }

    execCommand = (command: Command) => {
        for (let ix = 0; ix < command.count; ix++) {
            this.moveBetweenColumns(command.source, command.destination);
        }
    };

    getTopOfEachStack = () => {
        let result: string = "";
        this.columnStateMap.forEach((col) => {
            result = result + col.at(-1);
        });
        return result;
    };

    private moveBetweenColumns = (source: number, destination: number) => {
        let toTransfer = this.columnStateMap.get(source)?.pop();
        if (toTransfer !== undefined) {
            this.columnStateMap.get(destination)?.push(toTransfer);
        }
    };

    private pushToColumn = (column: number, item: string) => {
        if (item.trim() !== "") {
            if (this.columnStateMap.get(column) === undefined) {
                this.columnStateMap.set(column, []);
            }

            this.columnStateMap.get(column)?.push(item);
        }
    };
}

const main = () => {
    // read in file and drop any blank lines from the end
    let lineList: string[] = fs
        .readFileSync("../input.txt", "ascii")
        .split("\n");
    lineList = lodash.dropRightWhile(lineList, (line) => line.trim() === "");

    // starting state is everything up to the first blank line, minus the column header row
    let startingState = lodash
        .takeWhile(lineList, (line) => line.trim() !== "")
        .slice(0, -1);
    let commandList = lodash.dropWhile(
        lineList,
        (line) => !line.startsWith("move")
    );

    // init state
    let state: State = new State(startingState);
    let parsedCommandList: Command[] = commandList.map(
        (cmdText) => new Command(cmdText)
    );

    // run all the commands against the state object
    parsedCommandList.forEach(state.execCommand);

    console.log("top of each stack => " + state.getTopOfEachStack());
};

main();
