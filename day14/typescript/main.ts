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
    canMoveDown = (
        collisionSet: Set<string>,
        floorPlaneY?: number
    ): boolean => {
        if (floorPlaneY !== undefined)
            return (
                !collisionSet.has(this.getDownCoord()) &&
                this.y + 1 != floorPlaneY
            );
        else return !collisionSet.has(this.getDownCoord());
    };
    canMoveDownLeft = (
        collisionSet: Set<string>,
        floorPlaneY?: number
    ): boolean => {
        if (floorPlaneY !== undefined)
            return (
                !collisionSet.has(this.getDownLeftCoord()) &&
                this.y + 1 != floorPlaneY
            );
        else return !collisionSet.has(this.getDownLeftCoord());
    };
    canMoveDownRight = (
        collisionSet: Set<string>,
        floorPlaneY?: number
    ): boolean => {
        if (floorPlaneY !== undefined)
            return (
                !collisionSet.has(this.getDownRightCoord()) &&
                this.y + 1 != floorPlaneY
            );
        else return !collisionSet.has(this.getDownRightCoord());
    };
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

const partOne = (startingCollisionSet: Set<string>, voidPlaneY: number) => {
    const collisionSet = startingCollisionSet;
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

const partTwo = (startingCollisionSet: Set<string>, floorPlaneY: number) => {
    const collisionSet = startingCollisionSet;
    let sandCount = 0;

    while (!collisionSet.has("500,0")) {
        const currentSand = new Coordinate("500,0");
        let isStuck = false;

        while (!isStuck) {
            if (currentSand.canMoveDown(collisionSet, floorPlaneY)) {
                currentSand.moveDown();
            } else if (currentSand.canMoveDownLeft(collisionSet, floorPlaneY)) {
                currentSand.moveDownLeft();
            } else if (currentSand.canMoveDownRight(collisionSet, floorPlaneY)) {
                currentSand.moveDownRight();
            } else {
                // down did exist in collision set
                isStuck = true;
                sandCount++; // only count a sand if it came to rest
            }
        }

        collisionSet.add(currentSand.toString());
    }

    console.log("sand count before hitting 500,0 : " + sandCount);
};

const getLargestYCoord = (startingCollisionSet: Set<string>): number => {
    let largestY = 0;
    startingCollisionSet.forEach((it) => {
        var testCoord = new Coordinate(it);
        if (testCoord.y > largestY) largestY = testCoord.y;
    });

    return largestY;
};

const main = () => {
    const lineList: string[] = fs
        .readFileSync("../input.txt", "ascii")
        .split("\n");

    const collisionSetPartOne = generateCollisionSet(lineList);
    const collisionSetPartTwo = new Set<string>(collisionSetPartOne);

    partOne(collisionSetPartOne, getLargestYCoord(collisionSetPartOne) + 2);
    partTwo(collisionSetPartTwo, getLargestYCoord(collisionSetPartTwo) + 2);
};

main();
