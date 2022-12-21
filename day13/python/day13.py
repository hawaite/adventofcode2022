from enum import Enum

debug_out = False
class RuleEvaluationResult(Enum):
    CORRECT_ORDER = 1
    INCORRECT_ORDER = 2
    CONTINUE = 3

def debug(printStr):
    if debug_out:
        print(printStr)

def testBothInt(left, right):
    if left < right:
        debug("left '" + str(left) + "' is less than right '" + str(right) + "' so correct order immediately")
        return RuleEvaluationResult.CORRECT_ORDER
    if left > right:
        debug("left '" + str(left) + "' is more than right '" + str(right) + "' so wrong order immediately")
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
    
    # we're still on 'continue' at this point after running out of one of the lists
    if len(left) < len(right):
        debug("left ran out of items before right so correct order immediately")
        currentResult = RuleEvaluationResult.CORRECT_ORDER
    if len(left) > len(right):
        debug("right ran out of items before left so wrong order immediately")
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


def testPairInCorrectOrder(left, right):
    currentResult = RuleEvaluationResult.CONTINUE
    shortestListLength = min(len(left), len(right))
    i = 0
    while( currentResult == RuleEvaluationResult.CONTINUE and i < shortestListLength):
        if isinstance(left[i],int) and isinstance(right[i],int):
            debug("straight int compare")
            debug("compare " + str(left[i]) + " & " + str(right[i]))
            currentResult = testBothInt(left[i], right[i])
            debug(currentResult)
            debug("----")
        elif isinstance(left[i],list) and isinstance(right[i],list):
            debug("straight list compare")
            debug("compare " + str(left[i]) + " & " + str(right[i]))
            currentResult = testBothLists(left[i], right[i])
            debug(currentResult)
            debug("----")
        else:
            # some sort of mix
            debug("mixed input compare")
            debug("compare " + str(left[i]) + " & " + str(right[i]))
            currentResult = testListAndIntMix(left[i], right[i])
            debug(currentResult)
            debug("----")
        i+=1
    
    if(currentResult == RuleEvaluationResult.CONTINUE):
        debug("ran out of pair-wise comparisons")
        # either all items were the same, which I'm counting as correct ordering
        # or we ran out of items on one side
        if len(left) < len(right):
            debug("left ran out of items before right so correct order immediately")
            currentResult = RuleEvaluationResult.CORRECT_ORDER
        elif len(left) > len(right):
            debug("right ran out of items before left so wrong order immediately")
            currentResult = RuleEvaluationResult.INCORRECT_ORDER
        else:
            debug("the two items were equal. counting as correct")
            currentResult = RuleEvaluationResult.CORRECT_ORDER

    debug("final result : " + str(currentResult))
    return currentResult

def partOne(fileLines):
    pairs = fileLines.split('\n\n')
    correct_index_sum = 0
    for ix, pair in enumerate(pairs):
        pairIndex = ix + 1
        splitPair = pair.split('\n')
        debug("==== pair "+str(pairIndex) + " ====")
        debug(splitPair)
        result = testPairInCorrectOrder(eval(splitPair[0]), eval(splitPair[1]))
        if(result == RuleEvaluationResult.CORRECT_ORDER):
            correct_index_sum += pairIndex

    print("sum of correct indicies = " + str(correct_index_sum))

def partTwo(fileLines):
    pairs = fileLines.split('\n')
    filtered = list(filter(lambda x: x != '', pairs))
    filtered.append('[[2]]')
    filtered.append('[[6]]')
    debug(filtered)
    extremelyLazyInplaceKindOfBubbleSort(filtered)
    debug(filtered)
    overallResult = (filtered.index('[[6]]') + 1) * (filtered.index('[[2]]') + 1)
    print("overall result = " + str(overallResult))

def extremelyLazyInplaceKindOfBubbleSort(items):
    isSorted = False
    while not isSorted :
        isSorted = True #Assume true until proven otherwise. Could be smarter.
        for i in range(0,len(items)-1):
            result = testPairInCorrectOrder(eval(items[i]), eval(items[i+1]))
            if result == RuleEvaluationResult.INCORRECT_ORDER:
                # swap em
                temp = items[i]
                items[i] = items[i+1]
                items[i+1] = temp
                isSorted = False

def main():
    with open("../input.txt", "r") as fp:
        allLines = fp.read()
    #partOne(allLines)
    partTwo(allLines)


if __name__ == "__main__":
    main()