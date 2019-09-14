/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-09-14
 */

class Keys <T> (map: Map<T, Any>): Lazy<Set<T>> {
    override val value = map.keys
    override fun isInitialized() = true
}

class Loading(val url: String): Lazy<String> {
    private var v = "loading"
    override val value: String get() {
        if (v == "loading") {
//           ajax(url) {
//               v = it
//           }
        }
        return v
    }
    override fun isInitialized() = true
}

class CustomKeys(val map: Map<String, Any>) {
    val keys by Keys(map)
//    val data by Loading("a.json")
}