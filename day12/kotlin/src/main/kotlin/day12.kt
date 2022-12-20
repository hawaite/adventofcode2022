

import java.util.PriorityQueue
import java.io.File

val eHeight = 25

fun charToHeight(heightChar:Char):Int {
    if(heightChar == 'S')
        return 0
    else if(heightChar == 'E')
        return eHeight
    else
        return heightChar.code - 97
}

fun nodeLabelFromCoord(x:Int, y:Int):String = "(" + x + "," + y + ")"


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
    

    // sort priority queue by distance
    val compareByDistance: Comparator<Node> = compareBy { it.shortestDistanceFromStartNode }

    val unvisited = ArrayList<Node>()
    val nodeMap = HashMap<String,Node>()
    var startingNodeLabel:String = "";
    var endingNodeLabel:String = "";

    var row = 0
    var col = 0
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

    // read file in to a big pile of nodes with names based on 2d coords
    // add all those nodes to the unvisited list
    while( ! unvisited.isEmpty() ){
        unvisited.sortWith(compareByDistance)
        var currentNode = unvisited.removeAt(0)
        // process neighbours
        // println("testing node: " + currentNode.label)
        //test up
        if(currentNode.y != 0){
            // can test up
            var upNode = nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y - 1))
            // test not in visited list
            // valid if difference is 1 to -inf
            if (upNode != null && ! upNode.visited && ((upNode.height - currentNode.height) <= 1)){
                // this is a valid node to calculate
                // println("found valid up move to " + upNode.label)
                if((currentNode.shortestDistanceFromStartNode + 1) < upNode.shortestDistanceFromStartNode){
                    // println("this move is the new shortest path")
                    upNode.shortestDistanceFromStartNode = currentNode.shortestDistanceFromStartNode + 1
                    upNode.previous = currentNode.label
                }
            }
        }
        //test down
        if(currentNode.y != (rowMax - 1)){
            // can test down
            var downNode = nodeMap.get(nodeLabelFromCoord(currentNode.x, currentNode.y + 1))
            // test not in visited list
            // valid if difference is 1 to -inf
            if (downNode != null && ! downNode.visited && ((downNode.height - currentNode.height) <= 1)){
                // this is a valid node to calculate
                // println("found valid down move to " + downNode.label)
                if((currentNode.shortestDistanceFromStartNode + 1) < downNode.shortestDistanceFromStartNode){
                    // println("this move is the new shortest path")
                    downNode.shortestDistanceFromStartNode = currentNode.shortestDistanceFromStartNode + 1
                    downNode.previous = currentNode.label
                }
            }
        }
        //test left
        if(currentNode.x != 0){
            // can test left
            var leftNode = nodeMap.get(nodeLabelFromCoord(currentNode.x - 1, currentNode.y))
            // test not in visited list
            // valid if difference is 1 to -inf
            if (leftNode != null && ! leftNode.visited && ((leftNode.height - currentNode.height) <= 1)){
                // this is a valid node to calculate
                // println("found valid left move to " + leftNode.label)
                if((currentNode.shortestDistanceFromStartNode + 1) < leftNode.shortestDistanceFromStartNode){
                    // println("this move is the new shortest path")
                    leftNode.shortestDistanceFromStartNode = currentNode.shortestDistanceFromStartNode + 1
                    leftNode.previous = currentNode.label
                }
            }
        }
        //test right
        if(currentNode.x != (colMax - 1)){
            // can test right
            var rightNode = nodeMap.get(nodeLabelFromCoord(currentNode.x + 1, currentNode.y))
            // test not in visited list
            // valid if difference is 1 to -inf
            if (rightNode != null && ! rightNode.visited && ((rightNode.height - currentNode.height) <= 1)){
                // this is a valid node to calculate
                // println("found valid right move to " + rightNode.label)
                if((currentNode.shortestDistanceFromStartNode + 1) < rightNode.shortestDistanceFromStartNode){
                    // println("this move is the new shortest path")
                    rightNode.shortestDistanceFromStartNode = currentNode.shortestDistanceFromStartNode + 1
                    rightNode.previous = currentNode.label
                }
            }
        }
        // add to visited list
        currentNode.visited = true
        if(currentNode.label == endingNodeLabel){
            println("visited end node")
            break
        }
    }

    var currentTraceNode = nodeMap.get(endingNodeLabel)
    println("ending distance = " + currentTraceNode?.shortestDistanceFromStartNode) //520

    var path = ""
    while( currentTraceNode != null){
        path = currentTraceNode.label + " -> " + path
        currentTraceNode = nodeMap.get(currentTraceNode.previous)
    }

    println(path)
}