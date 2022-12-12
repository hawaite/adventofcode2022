import scala.io.Source
import scala.util.matching.Regex

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
    }

    class FileFsItem(name:String, size:Int) extends FsItem(name)
    
    var root = new DirectoryFsItem("/", null)
    var pwd = root

    def GetSubdirectoryFromCurrentDirectory(directoryName:String, pwd: DirectoryFsItem): Option[DirectoryFsItem] = {
        var foundSubDirectory:Option[FsItem] = pwd.children.find((fsItem) => {
            fsItem match{
                case d: DirectoryFsItem => fsItem.getName().equals(directoryName)
                case _ => false
            }
        })

        if(foundSubDirectory.isDefined){
            return Some(foundSubDirectory.get.asInstanceOf[DirectoryFsItem])
        }

        return None
    }

    def GetFileFromCurrentDirectory(fileName:String, pwd: DirectoryFsItem): Option[FileFsItem] = {
        var foundFile:Option[FsItem] = pwd.children.find((fsItem) => {
            fsItem match{
                case f: FileFsItem => fsItem.getName().equals(fileName)
                case _ => false
            }
        })

        if(foundFile.isDefined){
            return Some(foundFile.get.asInstanceOf[FileFsItem])
        }

        return None
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
            GetSubdirectoryFromCurrentDirectory(dirName, this.pwd).foreach(foundDir => this.pwd = foundDir)
        }
        else if( item.startsWith("dir ")){
            var dirName = item.drop(4)
            // add dir to pwd children if doesnt exist
            if(GetSubdirectoryFromCurrentDirectory(dirName, this.pwd).isEmpty){
                this.pwd.children = this.pwd.children :+ new DirectoryFsItem(dirName, this.pwd)
            }
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
                    var foundFile = GetFileFromCurrentDirectory(filename, this.pwd)
                    if( foundFile.isEmpty ){
                        this.pwd.children = this.pwd.children :+ new FileFsItem(filename, filesize.toInt)
                    }
                }
                case _ => throw new Exceptions.MalformedFileInputException
            }
        }
    }
}

object Main{

    def main(args: Array[String]):Unit = {
        Source.fromFile("../input_custom.txt", "ascii")
            .getLines()
            .foreach(FileSys.parseInputLine)
        
        FileSys.root.children.foreach(x => println(x.getName()))
    }
}