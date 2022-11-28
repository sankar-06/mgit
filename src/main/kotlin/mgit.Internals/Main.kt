package mgit.Internals

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64
import kotlin.reflect.typeOf
import java.nio.charset.*

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
        "read-tree" -> {
            if(args.size == 2) readTree(args[1])
        }
        "--help" -> {
            println("Involked help!")
        }
        else -> {
            println("Arguments missing. Use --help to see the arguments to be passed")
        }
    }
}