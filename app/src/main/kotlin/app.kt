import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document

/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-08-03
 */


fun app() {
    document.querySelector("#base")?.innerHTML = """
        <input id="input" />
        <div id="result"></div>
    """
    document.querySelector("#input")?.addEventListener("keyup", {
        if ((it as KeyboardEvent).keyCode != 13) return@addEventListener
        val input = it.target as HTMLInputElement
        val v = input.value
        document.querySelector("#result")?.innerHTML = "$v = ${calc(v)}"
        input.value = ""
    })
}

val cleanUp = """[^.\d-+*\/]""".toRegex()
val mulDiv = """((?:\+-)?[.\d]+)([*\/])((?:\+-)?[.\d]+)""".toRegex()
val paren = """\(([^()]*)\)""".toRegex()

// single expression funtion
fun ex(v: String) = v.replace(cleanUp, "").replace("-", "+-").replace(mulDiv) {
    val (_, left, op, right) = it.groupValues
    val l = left.replace("+", "").toDouble()
    val r = right.replace("+", "").toDouble()
    "${if (op == "*") l * r else l / r}".replace("-", "+-")
}.split('+').fold(0.0) { sum, v ->
    sum + if (v.isBlank()) 0.0 else v.toDouble()
}

fun calc(v: String): Double {
    var r = v
    while (paren.containsMatchIn(r)) r = r.replace(paren) { "${ex(it.groupValues[1])}" }
    return ex(r)
}
