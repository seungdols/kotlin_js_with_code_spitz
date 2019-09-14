import kotlin.properties.Delegates

/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-08-03
 */

fun main(args: Array<String>) {
// app()

    println(Test("a", "b").run { a + b })
    println(MapTest().run {
        this["test"] = "test0"
        this["test"]
    })
    println(MapTest().run {
        this["name"] = "seungdols"
        job = "Full-Stack developer"
        "job: $job, name: $name"
    })
    println(MapTest().run {
        //        fullName
        this["firstName"] = "seungdols"
        this["lastName"] = "choi"
        fullName
    })

//    htmlBuilder()
//    testFetch()

//    val list = ListA().plus("ABC")
//    val list = ListA() + "ABC"
    val list = ListA() + "ABC" add "def"
    println(list[0] + ", " + list[1])

//    val listCombine = 10 combine 20
//    println("${JSON.stringify(listCombine)}")
    val listCombine = 10 combine 20 combine 30 combine 40 combine mutableListOf(10,20)
    println("${JSON.stringify(listCombine)}")

    val v = NotNull()
    v.action("abc")

    val d = CustomDele("^___^;")
    d.action("seungdols")

    val l = CustomImmun("seungdols")
    println(l)

    val map = CustomKeys(mapOf("a" to 2, "b" to 3))
    println("${JSON.stringify(map.keys)}")
}

infix fun <T> T.combine(v: T) = mutableListOf(this, v)
infix fun <T> MutableList<T>.combine(v:T) = run {
    this += v
    this
}
infix fun <T> MutableList<T>.combine(v:MutableList<T>)  = run {
    this += v
    this
}

class NotNull {
//    var a: String by Delegates.notNull()
    lateinit var a: String
    fun action(v: String) {
        a = v
        println(a)
    }
}

fun parse() {
    printElement(
        parseHTML(
            """
    <div>
    test1
    <img/>
    test2
    <p a="3" b="abc">ptest</p>
    </div>
"""
        )
    )
}

class Test(val a: String, val b: String) {

}

class MapTest {
    private val map = mutableMapOf<String, String>()
    operator fun get(key: String) = map[key]
    operator fun set(key: String, value: String) {
        map[key] = value
    }

    val name: String? get() = map["name"]
    var job: String?
        get() = map["job"]
        set(value: String?) {
            value?.let { map["job"] = it }
        }
    val fullName by lazy { map["firstName"] + " " + map["lastName"] }
}

class ListA {
    val list = mutableListOf<String>()
    operator fun get(i: Int) = list[i]
    operator fun plus(b: String) = run {
        list += b
        this
    }

    infix fun add(b: String) = plus(b)
}

