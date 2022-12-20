import java.io.File

// used for testing on a limites alphabet range
val endNodeHeight = 25

fun charToHeight(heightChar:Char):Int = if (heightChar == 'S') 0 else if (heightChar == 'E') endNodeHeight else heightChar.code - 97

fun nodeLabelFromCoord(x:Int, y:Int):String = "(" + x + "," + y + ")"

// valid if difference is 1 to -inf
fun acceptableHeightDifference(current:Node, adjacent:Node) = ((adjacent.height - current.height) <= 1)

fun processAdjacentNode(current:Node, adjacent:Node?){
    if (adjacent != null && ! adjacent.visited && acceptableHeightDifference(current, adjacent)){
        // this is a valid node to calculate
        if((current.shortestDistanceFromStartNode + 1) < adjacent.shortestDistanceFromStartNode){
            adjacent.shortestDistanceFromStartNode = current.shortestDistanceFromStartNode + 1
            adjacent.previous = current.label
        }
    }
}

class Node(x:Int, y:Int, height:Int ) {
    val x: Int = x
    val y: Int = y
    val label: String = nodeLabelFromCoord(x,y)
    val height: Int = height
    var previous: String? = null
    var shortestDistanceFromStartNode: Int = Int.MAX_VALUE
    var visited: Boolean = false
}

fun main(args: Array<String>){
    val fileLines = File(args[0]).readLines()

    val compareByDistanceFromStartNode: Comparator<Node> = compareBy { it.shortestDistanceFromStartNode }

    val unvisited = ArrayList<Node>()
    val nodeMap = HashMap<String,Node>()

    var startingNodeLabel:String = "";
    var endingNodeLabel:String = "";

    var row = 0
    var col = 0

    // read in characters to create nodes
    for ( line in fileLines ){
        col = 0
        for ( character in line){
            var node = Node(col, row, charToHeight(character))

            if( character == 'S'){
                node.shortestDistanceFromStartNode = 0
                startingNodeLabel = node.label
            }
            else if ( character == 'E'){
                endingNodeLabel = node.label
            }
            unvisited.add(node)
            nodeMap.set(node.label, node)
            col++
        }
        row++
    }

    var rowMax = row
    var colMax = col

    println("starting node: " + startingNodeLabel)
    println("  ending node: " + endingNodeLabel)

    // Perform Dijkstra to get shortest path to E node
    while( ! unvisited.isEmpty() ){
        unvisited.sortWith(compareByDistanceFromStartNode)
        var currentNode = unvisited.removeAt(0)

        // process neighbours
        if(currentNode.y != 0) // can test up
            processAdjacentNode(currentNode, nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y - 1)))
        if(currentNode.y != (rowMax - 1)) // can test down
            processAdjacentNode(currentNode, nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y + 1)))
        if(currentNode.x != 0) // can test left
            processAdjacentNode(currentNode, nodeMap.get(nodeLabelFromCoord(currentNode.x - 1, currentNode.y)))
        if(currentNode.x != (colMax - 1)) // can test right
            processAdjacentNode(currentNode, nodeMap.get(nodeLabelFromCoord(currentNode.x + 1, currentNode.y)))

        // add to visited list
        currentNode.visited = true
        if(currentNode.label == endingNodeLabel){
            break
        }
    }

    var currentTraceNode = nodeMap.get(endingNodeLabel)
    println("ending distance = " + currentTraceNode?.shortestDistanceFromStartNode)
}