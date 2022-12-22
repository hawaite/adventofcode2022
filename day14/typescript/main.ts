import * as fs from "fs";
import _ from "lodash";

class Coordinate {
    x: number;
    y: number;

    constructor(input: string) {
        const parts = input.split(",");
        this.x = parseInt(parts[0]);
        this.y = parseInt(parts[1]);
    }

    getDownCoord = () => this.x + "," + (this.y + 1);
    getDownLeftCoord = () => this.x - 1 + "," + (this.y + 1);
    getDownRightCoord = () => this.x + 1 + "," + (this.y + 1);
    canMoveDown = (collisionSet: Set<string>): boolean =>
        !collisionSet.has(this.getDownCoord());
    canMoveDownLeft = (collisionSet: Set<string>): boolean =>
        !collisionSet.has(this.getDownLeftCoord());
    canMoveDownRight = (collisionSet: Set<string>): boolean =>
        !collisionSet.has(this.getDownRightCoord());
    moveDown = () => {
        this.y++;
    };
    moveDownLeft = () => {
        this.y++;
        this.x--;
    };
    moveDownRight = () => {
        this.y++;
        this.x++;
    };
    toString = () => this.x + "," + this.y;
}

const expandCoordinateRange = (start: string, end: string): string[] => {
    const startCoord = new Coordinate(start);
    const endCoord = new Coordinate(end);

    if (startCoord.x == endCoord.x) {
        // on same column, range over rows
        const ends = [startCoord.y, endCoord.y].sort();
        return _.range(ends[0], ends[1] + 1).map(
            (yPos) => startCoord.x + "," + yPos
        );
    } else if (startCoord.y == endCoord.y) {
        // on same column, range over rows
        const ends = [startCoord.x, endCoord.x].sort();
        return _.range(ends[0], ends[1] + 1).map(
            (xPos) => xPos + "," + startCoord.y
        );
    } else {
        throw Error("Coordinates had neither x or y in common");
    }
};

const generateCollisionSet = (lineList: string[]): Set<string> => {
    const collisionSet = new Set<string>();
    _.map(lineList, (it) => it.split(" -> ")).forEach((it) => {
        for (let i = 0; i < it.length - 1; i++) {
            expandCoordinateRange(it[i], it[i + 1]).forEach((it) =>
                collisionSet.add(it)
            );
        }
    });
    return collisionSet;
};

const main = () => {
    const lineList: string[] = fs
        .readFileSync("../input.txt", "ascii")
        .split("\n");

    const collisionSet = generateCollisionSet(lineList);
    // TODO: set the to something slightly lower than the lowest coord in collision set
    const voidPlaneY = 1000;

    let sandCount = 0;
    let sandVoided = false;

    while (!sandVoided) {
        const currentSand = new Coordinate("500,0");
        let isStuck = false;

        while (!isStuck && !sandVoided) {
            if (currentSand.canMoveDown(collisionSet)) {
                currentSand.moveDown();
                if (currentSand.y == voidPlaneY) {
                    sandVoided = true;
                }
            } else if (currentSand.canMoveDownLeft(collisionSet)) {
                currentSand.moveDownLeft();
            } else if (currentSand.canMoveDownRight(collisionSet)) {
                currentSand.moveDownRight();
            } else {
                // down did exist in collision set
                isStuck = true;
                sandCount++; // only count a sand if it came to rest
            }
        }

        collisionSet.add(currentSand.toString());
    }

    console.log("sand count before voiding: " + sandCount);
};

main();
