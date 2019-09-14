/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-09-14
 */
class Immun(override val value: String): Lazy<String> {
    override fun isInitialized() = true
}

class CustomImmun(v: String) {
    val a by Immun(v)
    fun action(v: String) {
        println(a)
    }
}
