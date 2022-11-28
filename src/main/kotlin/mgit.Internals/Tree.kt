package mgit.Internals

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Base64
import kotlin.reflect.typeOf
import java.nio.charset.*

fun writeTree(path:String = "."):String{
    var ignore = mutableListOf("./.mgit","./mgit.jar","./Main.kt","./Tree.kt","./Basic.kt")
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

fun inExcludedDirs(file: File): Boolean {
    val excludedDirs = listOf(".mgit")
    var current: File? = file.parentFile
    while (current != null) {
        if (excludedDirs.contains(current.name)) {
            return true
        }
        current = current.parentFile
    }
    return false
}

fun emptyTree(){
    val excludedfiles = listOf(".\\Main.kt",".\\Basic.kt",".\\mgit.jar",".\\Tree.kt")
    val excludeddirs = listOf(".",".\\.mgit")
    File(".").walkBottomUp().filter{file-> file.isFile && !inExcludedDirs(file)
    }.forEach {
        if(it.toString() !in excludedfiles) {
            it.deleteRecursively()
        }
    }
    File(".").walkBottomUp().filter{file-> !inExcludedDirs(file)
    }.forEach {
        if(it.isDirectory){
            if(it.toString() !in excludeddirs){
                it.deleteRecursively()
            }
        }
    }
}

fun iterTreeItems(oid:String) :MutableList<String>{
    var tree = catFile(oid,"tree")
    var tree_contents:MutableList<String> = tree.split("\n").toMutableList()
    println(tree_contents.removeAt(tree_contents.size - 1))
    return tree_contents
}

fun getTree(tree_oid: String,path: String=""):MutableMap<String,String>{
    var tree_contents = iterTreeItems(tree_oid)
    val result = mutableMapOf<String,String>()
    for (item in tree_contents){
        var (_type,oid,path)= item.split(" ")
//        println(_type + oid + path)
        if(_type == "blob"){
            result[path] = oid
        }
        else {
            result.putAll(getTree(oid,path))
        }
    }
    return result
}

fun readTree(tree_oid:String){
    emptyTree()
    var result = getTree(tree_oid)
    for ((key,value) in result ){
        var path:String = key
        path = path.substring(0,path.lastIndexOf("/"))
        File(path).mkdirs()
        val file = File(key)
        file.createNewFile()
        file.writeText(catFile(value))
    }
}