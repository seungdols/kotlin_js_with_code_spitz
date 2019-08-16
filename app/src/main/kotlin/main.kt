/**
 *
 * @PACKAGE
 * @AUTHOR  seungdols
 * @DATE    2019-08-03
 */

fun main(args: Array<String>) {
// app()
printElement(parseHTML("""
    <div>
    test1
    <img/>
    test2
    <p a="3" b="abc">ptest</p>
    </div>
"""))
}