
## Continuation & CPS (Continuation Passing Style)

* `continuation`은 어려운것은 아니고, callback과 부합한다.
* CPS는 callback을 Pass하는 형식으로 프로그래밍 하는 방식을 말한다. 
* 둘다 고유 명사이다. 
* 상황에 따라 Blocking 인지, Non-Blocking인지 구분이 된다. (상대적이다.)
  * 실제 머신은 모두 Blocking으로 처리를 한다. 

```kotlin
fun fact(n:Int) = _fact(n,1)
tailrec fun _fact(n: Int, a: Int): Int = if (n == 0) a else _fact(n-1, n * a)
```

위의 팩토리얼 함수를 CPS로 바꿔 본다면, 어떻게 될까? 

```kotlin
fun factCPS(n: Int, block: (Int) -> Unit) = _factCPS(n , 1, block)
tailrec fun _factCPS(n: Int, a: Int, block: (Int)-> Unit) {
    if (n == 0) block(a) else _factCPS(n - 1, n*a, block)
}

factCPS(3, ::println)
```

위의 함수를 보면, `_factCPS`의 마지막 인자를 보면, `block`을 항상 전달 하고 있다. 그리고 결과를 마지막에 항상 `Continuation`에 전달하면서 끝난다.  

이런 방법을 어디서 많이 사용 하느냐? 하면, `Error`에서 많이 사용 한다. 

```kotlin
fun factThrow(n: Int) = _factThrow(n,1 )
fun _factThrow(n: Int, a: Int): Int = when {
    n < 0 -> throw Throwable("invalid value $n")
    n == 0 -> a
    else -> _fact(n -1, n * a)
}

try { 
    println(factThrow(-3))
} catch (e: Throwable) {
    println(e)
}
```

> 위의 구조에서는 꼬리 재귀를 사용 할 수 없는데, 각 언어마다 꼬리 재귀를 사용할 수 없는 조건들이 있는데, 확인 해보고 쓰는게 좋다.

 위에서는 n이 0보다 작을 때, Throw하는 경우에 대한 부분이 추가 되면서, 꼬리 재귀를 사용 할 수 없다.
 
 참고: https://kotlinlang.org/docs/reference/functions.html#tail-recursive-functions
 
kotlin에서는 반드시 catch 해야하는 예외를 언어적으로 없애버렸다. (catch의 비효율적인 이유)

```kotlin
fun factCPSEx(n: Int, block: (Int)-> Unit, ex: (String)-> Unit = {}) = _factCPSEx(n, 1, block, ex)
tailrec fun _factCPSEx(n: Int, a: Int, block: (Int)-> Unit, ex: (String) -> Unit) {
  when {
        n < 0 -> ex("invalid value $n")
        n == 0 -> block(a)
        else -> _factCPSEx(n -1, n * a, block, ex)
    }
}
factCPSEx(-3, ::println, ::println)
```
### CPS의 장점 

* 직접적인 reporting 시점을 고를 수 있다. 
* 예외를 포함한 기존의 제어를 Continuation 객체 내부에서 모두 처리가 가능하다.

## Continuation & Sequence 

```kotlin
class Count<T> {
    var state = 0
    var isCompleted = false
    var result: T? = null
    fun resume(v: T) {
        state++
        result = v
    }
    fun complete(v: T) {
        isCompleted = true
        result = v
    }   
}

fun continuation1(a: Int, cont: Cont<Int>? = null) = run {
    var v:Int
    val c = if (cont == null) {
       v = a 
       Cont() 
    } else {
        v = cont.result!!
        cont
    }
    when (c.state) {
        0 -> {
            v++
            println("state $v")
            c.resume(v)
        }
        1 -> {
            v++
            println("state $v")
            c.resume(v)
        }
        else -> {
            v++
            println("state $v")
            c.complete(v)
        }
    }
    c
}


var cont = continuation1(3)
while(!cont.isCompleted) {
    cont = continaution1(3, cont)
}
println(cont.result)
```

## Sequence to Iteration 

* `kotlin`에서는 `sequence`가 고유 명사로, continuation 구문을 자동으로 만들어주는 iteration으로 뜻함. 

```kotlin
val s = sequence {
    var v = 3
    v++
    println("state $v")
    yield(v)
    v++
    println("state $v")
    yield(v)
    v++
    println("state $v")
    yield(v)
}
println(s.last())
```

위 코드로 작성을 하게 되면, `continuation` 형태로 짠 코드로 컴파일러가 바꿔 준다. 

## CO

* `coroutine`이 구현 된 시스템에서 수동으로 실행 함수를 만드는 기법이다. 
* kotlin의 yield는 단방향이지만, ES6의 generator는 양방향이 가능하다. 

```kotlin
class State {
    var result = ""
    lateinit var target: Promise<Response>
}

sequence {
    val s = State()
    s.target = window.fetch(Request("a.txt"))
    yield(s)
    s.target = window.fetch(Request(s.result))
    yield(s)
    println(s.result)
}

fun co(it: Iterator<State>? = null, sep:SequenceScope<State>? = null) {
    // 인자 2개가 둘 다 null이면, co 함수를 실행하지 못하게 throw 한다.
    val iter = it ?: sep?.iterator() ?: throw Throwable("invalid")
    if (iter.hasNext()) iter.next().let {st ->
        st.target.then {it.text()}.then{
                st.result = it
                co(iter)
            }
        }
}

// use co fun 
co(sequence{
    val s = State()
    s.target = window.fetch(Request("a.txt"))
    yield(s)
    s.target = window.fetch(Request(s.result))
    yield(s)
    println(s.result)
})
```

## suspend & coroutine 

이 내용은 중요하지만, 이해하기 어렵기 때문에, 다시 듣는 걸 추천 한다. (대략 50분쯔음 부터 1시간 3분정도까지..)

* Kotlin Coroutines
  * 고유 명사 
  * CoroutineContext 
    * element를 담아두는 container로 이해하면 된다. 
  * Dispatcher 
    * co 함수와 같은 실행기 
  * Element 
  * job 
    * iteration object   
    * async 작업은 Deferred await()를 받으면, resume 하게 됨. 
* 보통 하나의 코루틴은 하나의 Dispatcher와 다수의 Job을 가지고 있지 않을까?  


```kotlin
suspend fun <T>task(block:((T)-> Unit) -> Unit): T = suspendCoroutine {
    cont: Continuation<T> -> block{cont.resume(it)}
}

suspend fun main() {
    println("a")
    println(task{it("b")})
    println("c")
}

suspend fun timeout(t: Int): Unit = task{window.setTimeout({it(Unit)}, t)}

suspend fun main() {
    println("a")
    timeout(1000)
    println("b")
}
```

## suspendCancellableCoroutine

```kotlin
suspend fun <T>task(block:((T)-> Unit) -> Unit): T = suspendCoroutine {
    cont: Continuation<T> -> block{cont.resume(it)}
}

suspend fun <T> Promise<T>.await(): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> -> this.then(onFulfilled = { cont.resume(it)}, onRejected = { 
 cont.resumeWithException(it) }) }

suspend fun main() {
    val response1 = window.fetch(Request("a.txt")).await()
    val text1 = response1.text().await()
    val response2 = window.fetch(Request(text1)).await()
    val text2 = response2.text().await()
    println(text2)
}
```

* `ES6 async/await`의 경우에는 올 수 있는 타입이 `Promise`로 확정적이지만, `Kotlin Coroutine`은 자유롭게 구성이 가능하다. 

## launch & async 

* launch는 job을 만든다. 
* async는 deferred를 만든다.

```kotlin
suspend fun main() {
    GlobalScope.launch(
        // 기본 값이면, 컴파일러가 기본적인 job, dispatcher를 알아서 넣어 준다.
        context = EmptyCoroutineContext,
        // default를 하면, job의 start를 실행, lazy는 바로 실행하지 않고 직접 실행 해주어야 함.
        start = CoroutineStart.DEFAULT
    ){
        timeout(1000)
        println("a")
    }.join()
    GlobalScope.launch(
        context = Dispatchers.Default,
        start = CoroutineStart.LAZY
    ){
        timeout(1000)
        println("b")
    }.start()
}
```

* `LAZY`여도 start()를 하면 바로 실행 된다.
* `join()`을 하게 되면, `join()` 이후에 다음 job이 실행 된다. 
  * a 출력 -> 1초 대기 -> b 출력 
  * 위처럼 하게 되면, 순차적으로 실행하게 만들 수 있다. 
* 흐름이 위에서 아래로 흐르게 된다. 

```kotlin
   val a = GlobalScope.async(
        context = EmptyCoroutineContext,
        start = CoroutineStart.DEFAULT
    ){
        timeout(1000)
        "a"
    }
    val b = GlobalScope.async(
        context = Dispatchers.Default,
        start = CoroutineStart.DEFAULT
    ){
        timeout(1000)
        "b"
    }
    println(a.await())
    println(b.await())
```

* `launch`와는 다르게 내부의 값을 외부에 출력 시킬 수 있는 장점이 있다. 

```kotlin
fun <T>async(block: suspend CoroutineScope.()->T) = GlobalScope.async{block()}
fun launch(block: suspend CoroutineScope.()-> Unit) = GlobalScope.launch{block()}
```