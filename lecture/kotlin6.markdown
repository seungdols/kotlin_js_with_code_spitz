
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
        
    }
}
```
