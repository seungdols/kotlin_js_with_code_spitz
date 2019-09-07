
# class 

* kotlin의 클래스는 조금 특이한 부분이 있다. 
* `new`를 통해 객체를 만들지 않는다. 
* `open` 키워드를 붙이지 않으면, `final` 형태의 클래스가 만들어진다. 
  * `java`와는 다르게 기본적으로 상속이 안되도록 되어 있다. 
  * `public`은 명시 하지 않아도, 기본적으로 `public`이다.
  * `constructor`라는 키워드로 생성자 함수를 정의 한다. 

```kotlin
open public class Test {
    private val A:String
    private val B:String
    public constructor(a: String, B: String) {
        println("constructor1")
        A = a
        B = b
    }   
    public constructor(a: String).this(a, "b") {
        println("constructor2")
    }   
}
```

`constructor`의 1개를 제외하고는 `this`를 받아야 한다. (파생 생성자함수)

```kotlin
public class Test {
    private val A:String
    private val B:String
    constructor(a: String, B: String) {
        println("constructor1")
        A = a
        B = b
    }   
    constructor(a: String):this(a, "b") {
        println("constructor2")
    }   
}
```

아래처럼 변경하면, 해당 클래스는 `final public class`이다. 

```kotlin
public class Test constructor(a: String, B: String) {
    private val A:String
    private val B:String
    init {
        println("constructor1")
        A = a
        B = b
    }   
    constructor(a: String):this(a, "b") {
        println("constructor2")
    }   
}
``` 

`kotlin`은 기본 생성자 함수 시그니처를 클래스 이름 옆으로 이동 시킬 수 있다. 


## Super

```kotlin
open class Test(private val propA:String, private val propB:String) {
    constructor(a: String):this(a, "B")
}

class Test1:Test("A") {
    private val propC = "C"
}

class Test1(a: String, b: String, private val propC: String):Test(a, b)
```

## operator overloading

```kotlin
class Map { 
    private val map = mutableMapOf<String, String>()
    operator fun get(key: String) = map[key]
    operator fun set(key: String, value: String){map[key] = value}
}
```

```kotlin
class MapTest {
    private val map = mutableMapOf<String, String>()
    operator fun get(key: String) = map[key]
    operator fun set(key: String, value: String){map[key] = value}
    val name: String? get() = map["name"]
    var job: String? get() = map["job"]
                     set(value: String?) {value?.let { map["job"] = it }}
}
```

* Supported operator가 정말 많다. 참고: https://kotlinlang.org/docs/reference/operator-overloading.html

## by, by lazy

```kotlin

class MapTest {
    private val map = mutableMapOf<String, String>()
    operator fun get(key: String) = map[key]
    operator fun set(key: String, value: String){map[key] = value}
    val name: String? get() = map["name"]
    var job: String? get() = map["job"]
                     set(value: String?) {value?.let { map["job"] = it }}
    val name by lazy{map["firstName"] + " " + map["lastName"]}
}
```

* 사실 `=`과 `by`는 동일 하다. 
* `by` 뒤에 있는 걸 변수에 할당 할 때는 어떤 확정적 값 형태가 아닌 경우는 by로 할당 한다. (실제로는 프록시가 할당 된다.)
  * `kotlin`에서 확정 짓고 있는 `Delegate Object`만 올 수 있다.
  * `Kotlin Delegate Object`는 `getter/setter` 혹은 `getter`만 있는 오브젝트 일 수 있다. `get value` 형태를 생략하고 by로 매핑해서 사용 할 수 있음.
     * `proxy object`과 같은데, 실제로는 `placeholder object`이다. 
 
 * lazy는 실제 값을 미리 만들지 않고, 해당 값이 속성으로 최초 사용 할 시점에 생성 된다. (즉 name은 class 생성 시점과는 무관하다고 이해 하면 될까?)
  


## static

```kotlin
class Parent {
    companion object {
        fun action() {}
    }
}
```

`kotlin`에서는 `companion object`를 만들고 그 안에서 선언하게 되면 모두 `static`이 된다. 

## anonymous class 

```java
abstract class Parent {

}
Parent child1 = new Parent(){}
```

```javascript
const Parent = class{}
const instance = new (class extends Parent{})()
```

```kotlin
abstract class Parent 
class ClassTest2{
    val child1 = object:Parent(){}
}
```
`object`라는 키워드에 익명 클래스에 인스턴스까지 생성 하는 일을 하는데, `object expression`이라고도 부른다. 

```kotlin
object Child1:Parent(){} // object declaration
```

## singleton 

```kotlin
class SingleTon {
    companion object {
    val INSTANCE by lazy{SingleTon()}    
    }
}

object Child1:Parent(){}

class Child1:Parent() {
    companion object{
        val INSTANCE by lazy{Child1()}    
    }
}
```

`object` 키워드를 사용 할 때는 컴파일 타임에는 존재 하지만, 런타임에는 호출 하기 이전 까지는 생성 되지 않는다. 
그리고, 원래는 아래처럼 명시적인 코드를 `object` 키워드 하나로 다 처리 해준다. (JVM에서는 동기 처리까지 모두 해줌)

## sealed class & enum 

```kotlin
enum class Color(val code: String) {
    Red("#f00"), Blue("#00f"), Green("#0f0")
}
```

단점은 초기화가 오래 걸린다. (안드로이드에서는 쥐약) 자바스크립트에서는 어차피 가짜 구조라 상관 없다. 

```kotlin
abstract class Color(val code: String) {
    object Red:Color("#f00")
    object Blue:Color("#00f")
    object Green:Color("0f0")
}
```

원래는 `object`가 `companion` 하위로 들어가야 하는데, 코틀린 언어단에서 축약 해준다. 


```kotlin
sealed class Color(val code: String) {
    object Red:Color("#f00")
    object Blue:Color("#00f")
    object Green:Color("0f0")
    class Custom(code: String):Color(code) // nested object (ok)
}

object Yellow:Color("#ff0") // X 
```

`sealed class`로 만들면 더 이상의 상속 객체가 생기는 것을 막을 수 있다. 코틀린에서는 `enum class` 보다 보통 `sealed class`를 권장 한다.


