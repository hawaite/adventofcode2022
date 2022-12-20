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

class Graph{
    val unvisited = ArrayList<Node>()
    val nodeMap = HashMap<String,Node>()
    private val compareByDistanceFromStartNode: Comparator<Node> = compareBy { it.shortestDistanceFromStartNode }
    var startingNodeLabel:String = "";
    var endingNodeLabel:String = "";
    var rowMax = 0
    var colMax = 0

    fun parseFromLines(inputLines:List<String>,ignoreDefaultStartPosition:Boolean=false){
        var col = 0
        var row = 0

        for ( line in inputLines ){
            col = 0
            for ( character in line){
                var node = Node(col, row, charToHeight(character))
    
                if( character == 'S'){
                    if(!ignoreDefaultStartPosition)
                    node.shortestDistanceFromStartNode = 0
                    this.startingNodeLabel = node.label
                }
                else if ( character == 'E'){
                    this.endingNodeLabel = node.label
                }
                this.unvisited.add(node)
                this.nodeMap.set(node.label, node)
                col++
            }
            row++
        }
    
        this.rowMax = row
        this.colMax = col
    }

    fun resetGraph(){
        // reset node values and the unvisited list
        this.unvisited.clear()
        for(node in nodeMap.values){
            node.shortestDistanceFromStartNode = Int.MAX_VALUE
            node.visited = false
            this.unvisited.add(node)
        }
    }

    fun setStartNode(nodeLabel:String){
        this.startingNodeLabel = nodeLabel
        this.nodeMap.get(this.startingNodeLabel)?.shortestDistanceFromStartNode = 0
    }

    fun calculateShortestPath():List<String>?{
        // Perform Dijkstra to get shortest path to E node
        while( ! this.unvisited.isEmpty() ){
            this.unvisited.sortWith(compareByDistanceFromStartNode)
            var currentNode = this.unvisited.removeAt(0)

            if(currentNode.shortestDistanceFromStartNode == Int.MAX_VALUE){
                // there were no more valid nodes. bail.
                return null
            }
            
            // process neighbours
            if(currentNode.y != 0) // can test up
                processAdjacentNode(currentNode, this.nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y - 1)))
            if(currentNode.y != (this.rowMax - 1)) // can test down
                processAdjacentNode(currentNode, this.nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y + 1)))
            if(currentNode.x != 0) // can test left
                processAdjacentNode(currentNode, this.nodeMap.get(nodeLabelFromCoord(currentNode.x - 1, currentNode.y)))
            if(currentNode.x != (this.colMax - 1)) // can test right
                processAdjacentNode(currentNode, this.nodeMap.get(nodeLabelFromCoord(currentNode.x + 1, currentNode.y)))

            // add to visited list
            currentNode.visited = true
            if(currentNode.label == this.endingNodeLabel){
                break
            }
        }
        
        var traceNode = this.nodeMap.get(this.endingNodeLabel)
        var traceList = ArrayList<String>()

        while(traceNode != null){
            traceList.add(traceNode.label)
            traceNode = this.nodeMap.get(traceNode.previous)
        }

        return traceList
    }
}

fun main(args: Array<String>){
    val fileLines = File(args[0]).readLines()

    var graph = Graph()
    graph.parseFromLines(fileLines)

    println("starting node: " + graph.startingNodeLabel)
    println("  ending node: " + graph.endingNodeLabel)

    val pathResult = graph.calculateShortestPath()

    if(pathResult == null){
        println("No path between " + graph.startingNodeLabel + " and " + graph.endingNodeLabel)
    }
    else{
        println("ending distance from lst = " + (pathResult.size - 1))
    }
}