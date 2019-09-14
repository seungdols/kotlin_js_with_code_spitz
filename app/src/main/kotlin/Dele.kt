import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-09-14
 */

class Dele(val deco: String): ReadOnlyProperty<Any?, String> {
    private var value: String? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$deco$value"
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
    }
}

class CustomDele(deco: String) {
    var a by Dele(deco)
    fun action(v: String) {
        a = v
        println(a)
    }
}