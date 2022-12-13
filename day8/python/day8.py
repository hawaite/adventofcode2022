class Tree:
    def __init__(self, height):
        self.height = height
        self.upVisibility = False
        self.downVisibility = False
        self.leftVisibility = False
        self.rightVisibility = False

    def isVisible(self):
        return (self.upVisibility or self.downVisibility or self.leftVisibility or self.rightVisibility)

def checkRowVisibility(tree, idx, treeRow):

    leftElements = treeRow[0:idx]
    rightElements = treeRow[idx+1:]
    
    leftVisible = True
    for element in leftElements:
        if element.height >= tree.height:
            leftVisible = False
            break

    rightVisible = True
    for element in rightElements:
        if element.height >= tree.height:
            rightVisible = False
            break

    return (leftVisible, rightVisible)

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

    visibleTreeCount = 0

    #render
    for treeRow in treeArray:
        for tree in treeRow:
            if tree.isVisible():
                visibleTreeCount = visibleTreeCount + 1
            print(("T" if tree.isVisible() else "F"), end='')
        print()
    print("visible tree count => " + str(visibleTreeCount))

if __name__ == "__main__":
    main()