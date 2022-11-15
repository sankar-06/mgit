import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.reflect.typeOf

fun initDir(){
//  Creating .mgit folder
    var path = System.getProperty("user.dir") + "/.mgit"
    var file = File(path)
    if(file.isDirectory()){
        println("Reinitializing the repository")
    }else{
        file.mkdir()
        println("Initializing the repository")
    }

//  Creating the objects Directory
    path = System.getProperty("user.dir") + "/.mgit/objects"
    file = File(path)
    if(!file.isDirectory()){
        file.mkdir()
    }
}

fun catFile(objid:String){
    var path = System.getProperty("user.dir") + "/.mgit/objects/"
    if(File(path).exists()){
        path = System.getProperty("user.dir") + "/.mgit/objects/" + objid
        if(File(path).exists()) {
            val buff = File(path).bufferedReader().readLines()
            var readText: String = ""
            buff.forEach {
                readText = readText + it + "\n"
            }
            print(readText)
        }else {
            println("No hash-object found in $path")
        }
    }else{
        println("Initialize the repository to work with mgit")
    }
}

fun hashObject(filename:String) {
    var path = System.getProperty("user.dir") + "/.mgit/objects"
    if(File(path).exists()){
//      Read the contents of the file
        if(File(filename).exists()){
            val buff = File(filename).bufferedReader().readLines()
            var input:String = ""
            buff.forEach {
                input = input + it + "\n"
            }

//          Generate the SHA-1 Hash object
            val md = MessageDigest.getInstance("SHA-1")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }

//          Write the SHA-1 Hash object to .mgit/objects
            var hashfile = File(".mgit/objects/" + hashtext)
            hashfile.createNewFile()
            hashfile.writeText(input)
        }else {
            println("Unable to read the file. File does not exist")
        }
    }else{
        println("Initialize the repository to work with mgit")
    }
}

fun main(args: Array<String>) {
    val argparser = args[0]
    when(argparser){
        "init" -> initDir()
        "hash-object" -> hashObject(args[1])
        "cat-file" -> catFile(args[1])
        "--help" -> {
            println("Involked help!")
        }
        else -> {
            println("Arguments missing. Use --help to see the arguments to be passed")
        }
    }
}