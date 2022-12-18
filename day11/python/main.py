from functools import reduce
import math

class Monkey:
    def __init__(self, name, startingItemList, operation, test, trueAction, falseAction):
        self.name = name
        self.inspectionCount = 0
        self.itemList = startingItemList
        self.operation = operation
        self.test = test
        self.trueAction = trueAction
        self.falseAction = falseAction

    def throwAllItems(self, monkeyMap, productOfDivisors, worryDivByThree=True):
        for item in self.itemList:
            self.inspectionCount = self.inspectionCount + 1
            old = int(item)
            old = old % productOfDivisors
            # perform "operation" on item to get its new value
            new = eval(self.operation)
            if worryDivByThree:
                # divide new value by 3 and take floor of that
                new = math.floor((new / 3))
            # take new value, mod it with "test"
            testResult = (new % int(self.test)) == 0
            # if zero, throw to true, otherwise throw to false
            if testResult:
                monkeyMap[self.trueAction].acceptThrownItem(new)
            else:
                monkeyMap[self.falseAction].acceptThrownItem(new)
        # clear list once all thrown
        self.itemList = []

    def acceptThrownItem(self, item):
        self.itemList.append(item)

    def __str__(self):
        lines = ""
        lines = lines + "name: " + self.name + "\n"
        lines = lines + "inspection count: " + str(self.inspectionCount) + "\n"
        lines = lines + "item list : " + str(self.itemList) + "\n"
        lines = lines + "operation: " + self.operation + "\n"
        lines = lines + "test: " + self.test + "\n"
        lines = lines + "if true, throw to : " + self.trueAction + "\n"
        lines = lines + "if false, throw to : " + self.falseAction
        return lines

def parseMonkeyLines(monkeyString):
    # Monkey 0:
    #   Starting items: 85, 77, 77
    #   Operation: new = old * 7
    #   Test: divisible by 19
    #     If true: throw to monkey 6
    #     If false: throw to monkey 7

    monkeyLines = monkeyString.splitlines();
    name = monkeyLines[0].strip()[7:][:-1]
    startingItems = eval("[" + monkeyLines[1].strip()[16:] + "]")
    operation = monkeyLines[2].strip()[17:]
    test = monkeyLines[3].strip()[19:]
    trueTestAction = monkeyLines[4].strip()[25:]
    falseTestAction = monkeyLines[5].strip()[26:]

    return Monkey(name, startingItems, operation, test, trueTestAction, falseTestAction)

def performRound(monkeys, productOfDivisors, partTwo):
    for i in range(0,len(monkeys)):
        monkeys[str(i)].throwAllItems(monkeys, productOfDivisors, not partTwo)

def main():
    monkeys = {}
    partTwo = True

    # with open("../input.txt", "r") as fp:
    with open("../input.txt", "r") as fp:
        allLines = fp.read()
        monkeyInput = allLines.split("\n\n");
        for monkeyLines in monkeyInput:
            monkey = parseMonkeyLines(monkeyLines)
            monkeys[monkey.name] = monkey

    productOfDivisors = reduce((lambda x, y: x * y), [int(x.test) for x in monkeys.values()])

    rounds = 10000
    for i in range(0, rounds):
        # perform single round
        performRound(monkeys, productOfDivisors, partTwo)

    inspectionCounts = [x.inspectionCount for x in monkeys.values()]
    inspectionCounts.sort()
    print("monkey business = " + str( inspectionCounts[-2:-1][0] * inspectionCounts[-1:][0] ) )


if __name__ == "__main__":
    main()