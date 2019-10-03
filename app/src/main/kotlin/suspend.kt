import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @PACKAGE
 * @Author seungdols
 * @Date 2019-10-03
 */


suspend fun <T>task(block:((T)-> Unit) -> Unit): T = suspendCoroutine {
    cont: Continuation<T> -> block{cont.resume(it)}
}

suspend fun main() {
    println("a")
    println(task{it("b")})
    println("c")
}
