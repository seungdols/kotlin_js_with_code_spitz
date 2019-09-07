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
    testFetch()
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