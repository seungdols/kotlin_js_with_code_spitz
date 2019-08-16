/**
 * @PACKAGE
 * @Author seungdols
 * @Date 2019-08-16
 */


abstract class Node(val parent: Element?)

class Element(val tagName: String, parent: Element?) : Node(parent) {
    val attributes = mutableMapOf<String, String>()
    val children = mutableListOf<Node>()
}

class TextNode(val text: String, parent: Element?) : Node(parent)

val rex = """<([a-zA-Z]+)((?:\s+[a-zA-Z-]+(?:\s*=\s*"[^"]*")?)*)\s*/?""".toRegex()


fun parseHTML(v: String) = parse(Element("root", null), v)

//fun parse(parent: Element, v: String): Element {
//    if (v[0] != '<') {
//        // C
//        return if (v.isEmpty()) parent
//        else {
//            val next = v.indexOf('<')
//            parent.children += TextNode(v.substring(0, if (next == -1) v.length else next), parent)
//            if (next == -1) parent else parse(parent, v.substring(next))
//        }
//    } else {
//        val next = v.indexOf('>')
//        if (v[1] == '/') {
//            // A close
//            return if (parent.parent == null) parent
//            else parse(parent.parent, v.substring(next + 1))
//        } else {
//            val isClose = v[next - 1] == '/'
//            val matches = rex.matchEntire(v.substring(0, next))?.groupValues!!
//            val el = Element(matches[1], parent)
//            if(matches[2].isNotBlank()) matches[2].trim().split(' ').forEach {
//                val kv = it.split('=').map{it.trim()}
//                el.attributes[kv[0]] = kv[1].replace("\"", "")
//            }
//            parent.children += el
//            return parse(if(isClose) parent else el, v.substring(next + 1))
//        }
//    }
//}

tailrec fun parse(parent: Element, v: String): Element = if (v[0] != '<') {
    // C
    if (v.isEmpty()) parent
    else {
        val next = v.indexOf('<')
        parent.children += TextNode(v.substring(0, if (next == -1) v.length else next), parent)
        if (next == -1) parent else parse(parent, v.substring(next))
    }
} else {
    val next = v.indexOf('>')
    if (v[1] == '/') {
        // A close
        if (parent.parent == null) parent
        else parse(parent.parent, v.substring(next + 1))
    } else {
        val isClose = v[next - 1] == '/'
        val matches = rex.matchEntire(v.substring(0, next))?.groupValues!!
        val el = Element(matches[1], parent)
        if (matches[2].isNotBlank()) matches[2].trim().split(' ').forEach {
            if(it.contains('=')) {
                val kv = it.split('=').map { it.trim() }
                el.attributes[kv[0]] = kv[1].replace("\"", "")
            }else{
                el.attributes[it] = "true"
            }
        }
        parent.children += el
        parse(if (isClose) parent else el, v.substring(next + 1))
    }
}

fun printElement(el: Element, indent:Int = 0) {
    el.children.forEach {
        if (it is Element){
            println("${"-".repeat(indent)}Element ${it.tagName}")
            if(it.attributes.isNotEmpty()){
                println("${" ".repeat(indent + 2)}Attribute ${it.attributes.map{(k,v)-> "$k = '$v'"}.joinToString { " " }}")
            }
            printElement(it, indent + 1)
        } else if (it is TextNode){
            println("${"-".repeat(indent)}Text '${it.text}'")
        }
    }
}
