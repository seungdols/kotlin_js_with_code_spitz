
## Null / Nullable

```kotlin
val double: (Int) -> Int = {it * 2}

val v0: Int = 3 // if null -> compile error
println("${double(v0)}")
```

애초에 int 타입에 null이 없기에 컴파일 타임에 오류를 발생 시킨다. 
그래서 null을 사용하고자 한다면, 변수의 타입에 null을 받을 수 있게 해야 한다. 

```kotlin
val v1: Int? = null
println("${double(v1)}") // compile error double function 인자 타입과 다름.

if (v1 != null) println("${double(v1)}") // Int? -> Int smart cast를 해주기 때문에 가능하다. 
```

`?`을 이용해 nullable로 선언을 해주어야 한다. (무조건 컴파일 타임에 확정적이다.)
중요한건, nullable type과 일반 type과는 형이 일치 하더라도, nullable type은 다른 타입으로 인지 한다. 


## inline function 

컴파일러가 함수 호출 시점에 함수 본체를 가져와서 붙여 넣을 수 있다면, inline function 기능이 지원 된다. 

함수 호출이 잦은 경우에는 메모리와 연산을 교환해서 메모리를 늘리고, 연산을 줄이는 기법이 바로 inline function이다. 

다른 언어와는 다르게 inline function을 정말 많이 사용하는 언어가 kotlin이다. 

```kotlin
fun pass(v: Int, block:(Int)->Int) = block(v)

println("${pass(3){it * 2}}") // 6

inline fun pass(v: Int, block:(Int)->Int) = block(v)

inline fun ifTrue(v: Boolean, block:()-> Unit){if(v) block()}

inline fun <T> reverseFor(v:List<T>, block: (T) -> Unit){
    var i = v.size
    while(i-- > 9) block(v[i])
}

reverseFor(listOf("a", "b", "c"), ::println) // Function reference operator
```

컴파일 된 경우에는 제가 직접 작성한 코드와 유사하게 변환되어 컴파일 된다. 곧, 내가 마음대로 한들, 함수 호출 비용이 적다는 뜻.

## Extension function (extensions)

`C#`에서 가져온 기능이라고 한다. 공식 문서에서는 decorator 라고도 한다고 함. 

```kotlin
"   aaa   ".trim()

inline fun String.trim(): String = (this as CharSequence).trim().toString()
```

위에서 `String`을 Receiver라고 부른다. 그래서 첫번째 인자를 원하는 객체가 들어 오는데, 실제로 컴파일 되면, "aaa"가 함수의 첫번째 인자로 들어온다. 
수신 함수의 개념이다. 

이게 원래는 C와 C++의 시절까지 거슬러 올라간다고 함. (내용은 영상에서 참고 할 수 있다.)

```kotlin
inline fun <T> MutableList<T>.pop() = if(isEmpty()) null else removeAt(lastIndex)

var list = mutableListOf("a", "b", "c")
val last = list.pop()
println("last = $last, list = [${list.joinToString(",")}]")
```

## kotlin inline functions

kotlin에서는 class의 member function으로 있는 경우가 별로 없고, 모두 수신 함수의 형태로 다 나뉘어져 있다. 
그렇지만, 이 조각들이 컴파일 시점에서는 모두 조합 된다. 이 때 이 inline function들이 비용 발생이 없다. 

대부분 해당 inline function들이 있는데, 이들을 어느 정도 알아 두는 것이 좋다. 

```kotlin
inline fun TODO(): Nothing
inline fun TODO(reason: String): Nothing
inline fun <R> run(block: () -> R): R
inline fun <T, R> T.run(block: T.() -> R): R
inline fun <T, R> with(receiver: T, block: T.() -> R): R
inline fun <T> T.apply(block: T.() -> Unit): T
inline fun <T> T.also(block: (T) -> Unit): T
inline fun <T, R> T.let(block: (T) -> R): R
inline fun <T> T.takeIf(predicate: (T) -> Boolean): T?
inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T?
inline fun repeat(times: Int, action: (Int) -> Unit) 
```

```kotlin
inline fun <T, R> T.let(block: (T) -> R): R

val double: (Int) -> Int = {it * 2}

var v1: Int?  = null
v1?.let{
    println("${dobule(it)}")
}
```

let은 참 신기한 점은 인자가 null이면, body를 실행하지도 않고, 곧장 리턴 해버린다. 

```kotlin
val v2 = v1?.let(double(it)) ?: 0 
```

위처럼도 사용 할 수 있는데, v1이 null이면, let이 실행이 되지 않으니, v2에 null이 들어가게 되는데, 이를 막고, 0으로 넣고 싶을때 위처럼 사용하면 된다. 

`?:`는 elvis operator 라고 부른다. 

## Request Builder 

```kotlin

// before
val request = RequestBuilder("http://apiServer")
              .method(Method.POST)
              .form("name", "seungdols")
              .form("email", "seungdols0822@gmail.com")
              .timeout(5000)
              .ok{}
              .fail{}
              .build()

alias listener = (String) -> Unit
class Method{POST, GET}

class RequestBuilder(private val url:String) {
    private var method: Method = Method.GET
    private val form = mutableMapOf<String, String>()
    private var timeout = 0
    private var ok:listener? = null
    private var fail:listener? = null
    fun method(method: Method) {this.method = method}
    fun form(key: String, value: String){this.form[key] = value}
    fun timeout(ms:Int){this.timeout = ms}
    fun ok(block:listener){this.ok = block}
    fun fial(block: listener){this.fail = block}
    fun build() = Reuqest(url, method, form.takeIf{it.isNotEmpty()}, timeout, ok, faile) 
}

// after 

val request = with(RequestBuilder()){
   method(Method.POST)
    form("name", "seungdols")
    form("email", "seungdols0822@gmail.com")
    timeout(5000)
    ok{}
    fail{}
    build()
}


fun RequestBuilder(url:String, block: RequestBuilder.()-> Unit) = RequestBuilder(url).apply(block).build()

class RequestBuilder(private val url:String) {
    var method: Method = Method.GET
    private val form = mutableMapOf<String, String>()
    var timeout = 0
    var ok:listener? = null
    var fail:listener? = null
    fun form(key: String, value: String){this.form[key] = value}
}
```

class instance 생성시 `new`를 통해 생성하지 않는다. 그냥 함수 호출처럼 사용하면 된다. 

## typealias 

```kotlin
(String) -> Unit 

alias listener = (String) -> Unit
```

복잡하게 쓰이는 경우나, 반복 되는 경우 type에 대한 별칭을 지정할 수 있다. 그런데, 이름을 지정 할 수 있으니 가독성이 좋아지는 장점이 있다. 특히, 람다에 대해서 사용하는 것이 좋다. 


특정 공간이 특정 객체의 도메인 공간이 되도록 
수신함수를 이용해 DSL을 구축하고, DSL 공간에서 특정 객체의 어휘만 이용 하는 것이 kotlin DSL의 방식인데, 중요한건 주로 method chain도 드물다. 

```kotlin
val matches = rex.matchEntire(v.substring(0, next))?.groupValues!!
```

`!!`는 `not-null assertion operator`라고 하는데, `NPE`를 throw 해준다. 본래는 `try - catch`로 감싸서 처리를 해야 한다. 


`tailrec`의 키워드를 붙이면, 완전한 재귀함수는 for문으로 변경 된다. (컴팡일러가 알아서 바꿔준다.) 다만, Java만 해당 된다. 

`tailrec`의 형태로 작성하는 경우 리턴 되는 타입 형을 지정 해주는 습관을 들이면 좋다. 