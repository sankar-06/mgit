import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.reflect.typeOf

fun main(args: Array<String>) {
    if(args[0] == "init"){
//      Creating .mgit folder
        var path = System.getProperty("user.dir") + "/.mgit"
        var file = File(path)
        if(file.isDirectory()){
            println("Reinitializing the repository")
        }else{
            file.mkdir()
            println("Initializing the repository")
        }

//      Creating the objects Directory
        path = System.getProperty("user.dir") + "/.mgit/objects"
        file = File(path)
        if(!file.isDirectory()){
            file.mkdir()
        }
    }
    else if(args[0] == "hash-object"){
        var path = System.getProperty("user.dir") + "/.mgit"
        val file = File(path)
        if(file.exists()){
            if(args[1].length != 0) {
//              Read the contents of the file
                val filelocation = args[1]
                val buff = File(filelocation).bufferedReader().readLines()
                var input:String = ""
                buff.forEach {
                    input = input + it + "\n"
                }
//              Generate the SHA-1 Hash object
                val md = MessageDigest.getInstance("SHA-1")
                val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
//              Write the SHA-1 Hash object to .mgit/objects
                var hashfile = File(".mgit/objects/" + hashtext)
                hashfile.createNewFile()

                hashfile.writeText(input)
            }
        }else{
            println("No .mgit folder found. Initialize the repository to use git commands")
        }
    }else if(args[0] == "cat-file") {
        if (args[1].length != 0) {
            var path = System.getProperty("user.dir") + "/.mgit/objects/" + args[1]
            if(File(path).exists()) {
                val buff = File(path).bufferedReader().readLines()
                var input: String = ""
                buff.forEach {
                    input = input + it + "\n"
                }
                print(input)
            }else {
                println("No hash-object found in $path")
            }
        }
    }
}