import org.w3c.fetch.Request
import org.w3c.fetch.RequestInit
import kotlin.browser.window

/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-09-07
 */

class FetchParam {
    val queries = mutableMapOf<String, Any>()
    val headers = mutableMapOf<String, String>()
    var method = "GET"
}

fun fetch(url: String, block: FetchParam.() -> Unit) = FetchParam().apply { block() }.let {
    window.fetch(Request(url, RequestInit(
        method = it.method,
        headers = run {
            val obj = js("{}")
            it.headers.forEach { (k, v) ->
                obj[k] = v // dynamic object (do not touch compiler) }
                obj
            }
        },
        body = if (it.method != "GET") it.queries.toList().joinToString("&") { (k, v) -> "$k=$v" }
        else null
    )))
}

fun testFetch() {
    fetch("test.json"){}.then {
        it.text()
    }.then {
        println(it)
    }
}