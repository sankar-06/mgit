package mgit.Internals

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

