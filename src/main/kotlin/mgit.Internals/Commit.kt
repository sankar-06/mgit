package mgit.Internals

import java.io.File

fun setHead(oid : String){
    val head = File(".mgit/HEAD")
    head.createNewFile()
    head.writeText(oid)
}

fun getHead():String?{
    val path = ".mgit/HEAD"
    var head:String = ""
    if(File(path).exists()) {
        val obj = File(path).readBytes()
        head = obj.decodeToString()
    }
    return head
}
fun commit(message :String): String{
    var commitMessage = "tree :" + writeTree() +'\n'
    val head = getHead()
    if(head != null){
        commitMessage += "parent :"
        commitMessage += head
    }
    commitMessage += '\n'
    commitMessage += message



    val oid = hashObject(commitMessage.toByteArray(), "commit")
    setHead(oid)

    return oid
}
