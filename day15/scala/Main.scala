import scala.io.Source
import scala.util.matching.Regex
import scala.collection.immutable.Set
import java.util.ArrayList

object Util{
    def parseInputLine(inputLine:String):SensorBeaconPair = {
        val lineRegex = """^Sensor at x=(?<sensorX>-?\d+), y=(?<sensorY>-?\d+): closest beacon is at x=(?<beaconX>-?\d+), y=(?<beaconY>-?\d+)""".r
        inputLine match {
            case lineRegex(sensorX, sensorY, beaconX, beaconY) => {
                return new SensorBeaconPair(
                    new Coordinate(sensorX.toInt, sensorY.toInt),
                    new Coordinate(beaconX.toInt, beaconY.toInt)
                )
            }
            case _ => throw new Exception("Malformed input line: " + inputLine)
        }
    }

    def generateRowCenteredAt(center:Coordinate, count:Int):Set[Coordinate] = {
        if(count % 2 == 0)
            throw new Exception("Rows must be odd length")
        
        return ((center.x-(count/2)) to (center.x+(count/2)))
                    .map(x => new Coordinate(x, center.y))
                    .toSet[Coordinate]
    }
}
case class Coordinate(x:Int, y:Int)

class SensorBeaconPair(val sensor:Coordinate, val beacon:Coordinate){
    val manhattanDistance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y)

    def manhattanBoundingBoxWillContainRow(row:Int):Boolean = {
        val upperBound = sensor.y + manhattanDistance
        val lowerBound = sensor.y - manhattanDistance
        return (upperBound >= row) && (row >= lowerBound)
    }

    // def getManhattanBoxCoordinateSet():Set[Coordinate] = {
    //     // the y-coords that make up the full height of the box
    //     val yCoords = ((sensor.y - manhattanDistance) to (sensor.y + manhattanDistance))
    //     // the widths of the row for each y coord
    //     val widths = (1 to (manhattanDistance*2+1) by 2) ++ ((manhattanDistance*2)-1 to 1 by -2)

    //     return widths.zip(yCoords)
    //             .map( boxMetadataPair => Util.generateRowCenteredAt(new Coordinate(sensor.x, boxMetadataPair._2), boxMetadataPair._1) )
    //             .reduce((x,y) => x++y ).toSet[Coordinate]
    // }

    def getManhattanBoxCoordinateSetForSingleRow(row:Int):Option[Set[Coordinate]] = {
        // the y-coords that make up the full height of the box
        val yCoords = ((sensor.y - manhattanDistance) to (sensor.y + manhattanDistance))
        // the widths of the row for each y coord
        val widths = (1 to (manhattanDistance*2+1) by 2) ++ ((manhattanDistance*2)-1 to 1 by -2)

        val rowMetaData = widths.zip(yCoords).find(boxMetadataPair => boxMetadataPair._2 == row)
        if(!rowMetaData.isEmpty)
            return Option(Util.generateRowCenteredAt(new Coordinate(sensor.x, rowMetaData.get._2), rowMetaData.get._1).toSet[Coordinate])
        else
            return Option.empty
    }
}

object Main{
    def main(args: Array[String]):Unit = {
        
        val rowToCheck = 2000000
        val initialPositionSet:Array[SensorBeaconPair] = 
            Source.fromFile("../input.txt", "ascii")
            .getLines()
            .map(Util.parseInputLine)
            .filter(it => it.manhattanBoundingBoxWillContainRow(rowToCheck)) // cut down the problem space
            .toArray
            
        // get the set of all coordinates on the row in question making up the manhattan bounding box area for all sensor/beacon pairs
        var impossiblePositionSet = initialPositionSet
                                        .map(it=>it.getManhattanBoxCoordinateSetForSingleRow(rowToCheck))
                                        .filter(it => it.isDefined)
                                        .map(it => it.get)
                                        .reduce((x,y) => x ++ y )

        // // remove all the beacons from the "cant be beacons" positions
        impossiblePositionSet = impossiblePositionSet -- initialPositionSet.map(it => it.beacon)

        // // find all impossible possitions on given line
        println(s"number of impossible positions on row => ${impossiblePositionSet.filter(it => it.y == rowToCheck).size}")
    }
}