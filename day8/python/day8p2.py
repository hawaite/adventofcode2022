class Tree:
    def __init__(self, height):
        self.height = height
        self.upVisibility = 0
        self.downVisibility = 0
        self.leftVisibility = 0
        self.rightVisibility = 0

    def getScore(self):
        return self.upVisibility * self. rightVisibility * self.downVisibility * self.leftVisibility

    def __str__(self):
        return f"({self.height}): ^{self.upVisibility} <{self.leftVisibility} V{self.downVisibility} >{self.rightVisibility}"

def checkRowVisibility(tree, idx, treeRow):

    leftElements = treeRow[0:idx]
    rightElements = treeRow[idx+1:]
    
    leftVisibility = 0
    # reverse the left section so we are working out from the central tree
    for element in leftElements[::-1]:
        leftVisibility = leftVisibility + 1
        if element.height >= tree.height:
            break

    rightVisibility = 0
    for element in rightElements:
        rightVisibility = rightVisibility + 1
        if element.height >= tree.height:
            break

    return (leftVisibility, rightVisibility)

# rotates the grid left 90 degrees
def pivotGrid(treeArray):
    originalColumnsofTrees = len(treeArray[0])
    originalRowsOfTrees = len(treeArray)

    rotatedGrid = []
    for i in range(0,originalColumnsofTrees):
        newRow = []
        for j in range(0, originalRowsOfTrees):
            newRow.append(treeArray[j][i])
        rotatedGrid.insert(0, newRow)

    return rotatedGrid

def main():
    treeArray = []
    with open("../input.txt", "r") as fp:
        for line in fp.readlines():
            treeRow = []
            for char in line.strip():
                treeRow.append(Tree(int(char)))
            treeArray.append(treeRow)

    # set left/right visibility
    for row in treeArray:
        for idx, tree in enumerate(row):
            tree.leftVisibility, tree.rightVisibility = checkRowVisibility(tree, idx, row)

    # pivot grid 90 degrees left
    rotatedGrid = pivotGrid(treeArray)

    # set up/down visibility
    for row in rotatedGrid:
        for idx, tree in enumerate(row):
            tree.upVisibility, tree.downVisibility = checkRowVisibility(tree, idx, row)

    highestVisibilityScore = 0

    for treeRow in treeArray:
        for tree in treeRow:
            if(tree.getScore() > highestVisibilityScore):
                highestVisibilityScore = tree.getScore()

    print("highest visibility score => " + str(highestVisibilityScore))

if __name__ == "__main__":
    main()