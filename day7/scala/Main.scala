import scala.io.Source
import scala.util.matching.Regex
import scala.collection.mutable.{Map => MutableMap}

object Exceptions{
    class CannotCdToFileException extends RuntimeException
    class MalformedFileInputException extends RuntimeException
}

object FileSys{
    abstract class FsItem(name: String){
        def getName() = name
    }

    class DirectoryFsItem(name:String, parent:DirectoryFsItem) extends FsItem(name){
        var children:Array[FsItem] = Array()
        def getParent() = parent
        def getAbsolutePath():String = {
            if(getName().equals("/"))
                return ""
            else{
                return this.parent.getAbsolutePath() + "/" + getName()
            }
        }
    }

    class FileFsItem(name:String, size:Int) extends FsItem(name){
        def getSize() = size
    }

    var root = new DirectoryFsItem("/", null)
    var pwd = root
    var filesizeTotal = 0

    def getSubdirectoryFromCurrentDirectory(directoryName:String): Option[DirectoryFsItem] = {
        return this.pwd.children.find((fsItem) => {
            fsItem match{
                case d: DirectoryFsItem => fsItem.getName().equals(directoryName)
                case _ => false
            }
        }).map(_.asInstanceOf[DirectoryFsItem])
    }

    def getFileFromCurrentDirectory(fileName:String): Option[FileFsItem] = {
        this.pwd.children.find((fsItem) => {
            fsItem match{
                case f: FileFsItem => fsItem.getName().equals(fileName)
                case _ => false
            }
        }).map(_.asInstanceOf[FileFsItem])
    }

    def createFileInPwdIfDoesntExist(filename:String, filesize:Int) = {
        if( getFileFromCurrentDirectory(filename).isEmpty ){
            this.pwd.children = this.pwd.children :+ new FileFsItem(filename, filesize)
            this.filesizeTotal += filesize
        }
    }

    def createSubdirectoryInPwdIfDoesntExist(directoryName:String) = {
        if(getSubdirectoryFromCurrentDirectory(directoryName).isEmpty){
            this.pwd.children = this.pwd.children :+ new DirectoryFsItem(directoryName, this.pwd)
        }
    }

    def parseInputLine(input:String) = {
        var item = input.trim()
        // parse row in to tree structure

        // special root case
        if (item.equals("$ cd /")){
            this.pwd = root
        }
        else if (item.equals("$ cd ..")){
            this.pwd = this.pwd.getParent()
        }
        else if( item.startsWith("$ cd")){
            var dirName = item.drop(5)
            // change pwd to a child of current pwd with the name provided
            getSubdirectoryFromCurrentDirectory(dirName).foreach(foundDir => this.pwd = foundDir)
        }
        else if( item.startsWith("dir ")){
            var dirName = item.drop(4)
            // add dir to pwd children if doesnt exist
            createSubdirectoryInPwdIfDoesntExist(dirName)
        }
        else if( item.startsWith("$ ls")){
            // does nothing
        }
        else{
            // everything else must be a file
            // add file to current directory
            var fileRegex:Regex = """^(?<filesize>\d+) (?<filename>.*)$""".r
            item match {
                case fileRegex(filesize, filename) => {
                    createFileInPwdIfDoesntExist(filename, filesize.toInt)
                }
                case _ => throw new Exceptions.MalformedFileInputException
            }
        }
    }

    def getDirectorySizesFromRoot():MutableMap[String,Int] = {
        // for each directory in the current directory
        // calculate that directory size and sum it with the other directories
        var directorySizes:MutableMap[String,Int] = MutableMap()
        calculateDirectorySize(this.root, directorySizes)
        return directorySizes
    }

    def calculateDirectorySize(directory:DirectoryFsItem, directorySizes:MutableMap[String,Int]):Int = {

        // sum all files in current directory
        var currentDirectoryItemsSizes = directory
            .children
            .filter(_.isInstanceOf[FileFsItem])
            .map(_.asInstanceOf[FileFsItem])
            .map(x => x.getSize())

        // guarding for empty directories
        var currentDirectoryItemsSize = 0
        if(!currentDirectoryItemsSizes.isEmpty){
            currentDirectoryItemsSize = currentDirectoryItemsSizes.reduce((x1, x2) => x1 + x2)
        }
            
        // recursively calculate the size of subdirectories
        var currentDirectorySubdirectorySizes = directory
            .children
            .filter(_.isInstanceOf[DirectoryFsItem])
            .map(_.asInstanceOf[DirectoryFsItem])
            .map(x => calculateDirectorySize(x, directorySizes))

        // guarding for not having any subdirectories
        var currentDirectorySubdirectorySize = 0;
        if(!currentDirectorySubdirectorySizes.isEmpty){
            currentDirectorySubdirectorySize = currentDirectorySubdirectorySizes.reduce((x1, x2) => x1 + x2)
        }
            
        var totalCurrentDirectorySize = currentDirectoryItemsSize + currentDirectorySubdirectorySize
        directorySizes.addOne((directory.getAbsolutePath(), totalCurrentDirectorySize))

        return totalCurrentDirectorySize
    }
}

object Main{

    def main(args: Array[String]):Unit = {
        Source.fromFile("../input.txt", "ascii")
            .getLines()
            .foreach(FileSys.parseInputLine)

        var directorySizes = FileSys.getDirectorySizesFromRoot()

        println("part 1 result => " + directorySizes.filter((directoryTuple) => directoryTuple._2 <= 100000 ).map(_._2).reduce((x1, x2) => x1 + x2) )

        
        val diskSize = 70000000
        val requiredFreeSpace = 30000000
        val usedSpace = directorySizes.get("").get;
        val currentFreeSpace = diskSize - usedSpace
        val additionalDiskSpaceRequired = requiredFreeSpace - currentFreeSpace
        
        println("part 2 result => " + directorySizes.filter((dir) => {dir._2 >= additionalDiskSpaceRequired}).map((dir) => dir._2).min)
    }
}