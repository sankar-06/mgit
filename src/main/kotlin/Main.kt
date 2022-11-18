import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64
import kotlin.reflect.typeOf
import java.nio.charset.*

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

fun catFile(objid:String,expected: String="blob"):String{
    var path = System.getProperty("user.dir") + "/.mgit/objects/"
    var type_:String =" "
    var content:String =" "
    if(File(path).exists()){
        path = System.getProperty("user.dir") + "/.mgit/objects/" + objid
        if(File(path).exists()) {
            val obj = File(path).readBytes()
            var (a,b) = obj.decodeToString().split("\u0000")
            type_ = a
            content = b
        }else {
            println("No hash-object found in $path")
        }
    }else{
        println("Initialize the repository to work with mgit")
    }
    return content
}

fun hashString(input : ByteArray): String {
    val md = MessageDigest.getInstance("SHA-1")
    val messageDigest = md.digest(input)
    val no = BigInteger(1, messageDigest)
    var hashtext = no.toString(16)
    while (hashtext.length < 32) {
        hashtext = "0$hashtext"
    }
    return hashtext.toString()
}
fun hashTree(input: String,type_:String="tree"):String{
    var obj:ByteArray
    var oid:String = ""

//  Retrieve Tree object
    obj = type_.toByteArray(Charsets.UTF_8) + "\u0000".toByteArray(Charsets.UTF_8) + input.toByteArray(Charsets.UTF_8)
    oid = hashString(obj)
    return oid
}
fun hashObject(input: ByteArray,type_:String="blob"):String{
    var obj:ByteArray
    var oid:String = ""

//  Writing the contents to the file
    obj = type_.toByteArray(Charsets.UTF_8) + "\u0000".toByteArray(Charsets.UTF_8) + input
    oid = hashString(obj)
    var hashfile = File(".mgit/objects/" + oid)
    hashfile.createNewFile()
    hashfile.writeBytes(obj)
    return oid
}

fun writeTree(path:String = "."):String{
    var ignore = mutableListOf("./.mgit","./mgit.jar")
    var tree_contents: MutableList<MutableList<String>> = ArrayList()
    var type_ = ""
    var oid = ""

    File(path).list().forEach { it ->
        var newpath:String = path + '/' + it.toString()
        if (newpath !in ignore) {
            if(File(newpath).isDirectory){
                type_ = "tree"
//                oid = hashTree(newpath)
                oid = writeTree(newpath)
            }else {
                type_ = "blob"
                val input = File(newpath).readBytes()
                oid = hashObject(input)
            }
            var temp = mutableListOf<String>(type_,oid,newpath)
            tree_contents.add(temp)
        }
    }
    var tree:String = ""
    tree_contents.forEach { item->
        tree = tree + item[0] + " " + item[1] + " " + item[2] + "\n"
    }
    return hashObject(tree.toByteArray(Charsets.UTF_8),"tree")
}
fun main(args: Array<String>) {
    val argparser = args[0]
    when(argparser){
        "init" -> initDir()
        "hash-object" -> {
            var path = System.getProperty("user.dir")
            if(File(path).exists()) {
                val filename = System.getProperty("user.dir") +'/'+ args[1]
                if(File(filename).exists()) {
                    val input = File(filename).readBytes()
                    var oid = hashObject(input)
                }else{
                    println("No such file exisits.")
                }
            }else{
                println("Initialize the mgit repository.")
            }
        }
        "cat-file" -> {
            var content = catFile(args[1])
            println(content)
        }
        "write-tree" -> {
            if(args.size == 2) writeTree(args[1])
            else
                println(writeTree())
        }
        "--help" -> {
            println("Involked help!")
        }
        else -> {
            println("Arguments missing. Use --help to see the arguments to be passed")
        }
    }
}