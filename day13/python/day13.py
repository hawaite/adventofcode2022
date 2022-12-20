from collections import namedtuple
from enum import Enum

MessagePair = namedtuple("MessagePair", "left right")

class RuleEvaluationResult(Enum):
    CORRECT_ORDER = 1
    INCORRECT_ORDER = 2
    CONTINUE = 3

def parsePair(pairString):
    splitPair = pairString.split('\n')
    return MessagePair(left=eval(splitPair[0]), right=eval(splitPair[1]))

def testBothInt(left, right):
    if left < right:
        print("left '" + str(left) + "' is less than right '" + str(right) + "' so correct order immediately")
        return RuleEvaluationResult.CORRECT_ORDER
    if left > right:
        print("left '" + str(left) + "' is more than right '" + str(right) + "' so wrong order immediately")
        return RuleEvaluationResult.INCORRECT_ORDER
    else:
        return RuleEvaluationResult.CONTINUE

def testBothLists(left, right):
    shortestListLength = min(len(left), len(right))

    currentResult = RuleEvaluationResult.CONTINUE
    for i in range(0,shortestListLength):
        if isinstance(left[i],int) and isinstance(right[i],int):
            currentResult = testBothInt(left[i], right[i])
            if currentResult != RuleEvaluationResult.CONTINUE:
                return currentResult
        elif isinstance(left[i],list) and isinstance(right[i],list):
            currentResult = testBothLists(left[i], right[i])
            if currentResult != RuleEvaluationResult.CONTINUE:
                return currentResult
        else:
            currentResult = testListAndIntMix(left[i], right[i])
            if currentResult != RuleEvaluationResult.CONTINUE:
                return currentResult
    
    # we're still on 'continue at this point'
    if len(left) < len(right):
        print("left ran out of items before right so correct order immediately")
        currentResult = RuleEvaluationResult.CORRECT_ORDER
    if len(left) > len(right):
        print("right ran out of items before left so wrong order immediately")
        currentResult = RuleEvaluationResult.INCORRECT_ORDER

    return currentResult

def testListAndIntMix(left, right):
    left_updated = left
    right_updated = right
    if isinstance(left,int):
        left_updated = [left]
    elif isinstance(right,int):
        right_updated = [right]

    return testBothLists(left_updated, right_updated)


def testPairInCorrectOrder(pair:MessagePair):
    currentResult = RuleEvaluationResult.CONTINUE
    shortestListLength = min(len(pair.left), len(pair.right))
    i = 0
    while( currentResult == RuleEvaluationResult.CONTINUE and i < shortestListLength):
        if isinstance(pair.left[i],int) and isinstance(pair.right[i],int):
            print("straight int compare")
            print("compare " + str(pair.left[i]) + " & " + str(pair.right[i]))
            currentResult = testBothInt(pair.left[i], pair.right[i])
            print(currentResult)
            print("----")
        elif isinstance(pair.left[i],list) and isinstance(pair.right[i],list):
            print("straight list compare")
            print("compare " + str(pair.left[i]) + " & " + str(pair.right[i]))
            currentResult = testBothLists(pair.left[i], pair.right[i])
            print(currentResult)
            print("----")
        else:
            # some sort of mix
            print("mixed input compare")
            print("compare " + str(pair.left[i]) + " & " + str(pair.right[i]))
            currentResult = testListAndIntMix(pair.left[i], pair.right[i])
            print(currentResult)
            print("----")
        i+=1
    
    if(currentResult == RuleEvaluationResult.CONTINUE):
        print("ran out of pair-wise comparisons")
        # either all items were the same, which should never happen
        # of we ran out of items on one side
        if len(pair.left) < len(pair.right):
            print("left ran out of items before right so correct order immediately")
            currentResult = RuleEvaluationResult.CORRECT_ORDER
        if len(pair.left) > len(pair.right):
            print("right ran out of items before left so wrong order immediately")
            currentResult = RuleEvaluationResult.INCORRECT_ORDER

    print("final result : " + str(currentResult))
    return currentResult

def main():
    with open("../input.txt", "r") as fp:
        allLines = fp.read()
        pairs = allLines.split('\n\n')
        correct_index_sum = 0
        for ix, pair in enumerate(pairs):
            pairIndex = ix + 1
            parsedPair = parsePair(pair)
            print("==== pair "+str(pairIndex) + " ====")
            print(parsedPair)
            result = testPairInCorrectOrder(parsedPair)
            if(result == RuleEvaluationResult.CORRECT_ORDER):
                correct_index_sum += pairIndex

        print("sum of correct indicies = " + str(correct_index_sum))


if __name__ == "__main__":
    main()