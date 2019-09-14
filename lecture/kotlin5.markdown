## infix function 

* 전위 
* 후위 
* 중위 

함수(연산)를 호출 방식에 대한 이름을 말한다. 

대부분 원래 전위인데, 중위로 사용을 한다. 
`add(1,2) -> 1 + 2`를 사용하는 것과 같은데, 코틀린은 대부분의 전위 연산을 중위 연산으로 바꿀 수 있도록 해준다. 

단지, method만 가능하다는 제약이 있다. 

```kotlin
class ListA {
    val list = mutableListOf<String>()
    operator fun get(i:Int) = list[i]
    operator fun plus(b: String) = run {
        list += b
        this
    }   
}

val list = ListA() + "ABC"
println(list[0])
```

위와 같을 때 infix를 추가해 사용 해본다. 

```kotlin
    infix fun add(b: String) = plus(b)
```

infix를 추가 하여 사용하면 아래와 같이 사용할 수 있다. 

```kotlin
    val list = ListA() + "ABC" add "def"
```

infix는 연속적인 체이닝이 똑같은 일을 할 때 사용하면 좋다. 

```kotlin
infix fun <T> T.combine(v:T) = mutableListOf(this,v)

```

combine 하나로는 mutableList로 한 번 변환 되면, 다른 체이닝을 처리 할 수 없다. 

```kotlin
infix fun <T> T.combine(v: T) = mutableListOf(this, v)
infix fun <T> MutableList<T>.combine(v:T) = run {
    this += v
    this
}
```

위와 같이 하나의 infix function을 더 두면 여러개의 체이닝이 가능해진다. 

## Pair

```kotlin
public infix fun <A, B> A.to(that: B): Pair<A,B> = Pair(this, that)
val map = mnapOf("A" to 1, "B" to 2)
```

## mutable delegation 

* 속성, 맵, 클래스가 있다. 
  * 속성 
    * 2가지가 존재한다. (mutable, immutable)

### ReadWriteProperty 

```kotlin
public interface ReadWriteProperty<in R, T> {
    public operator fun getValue(thisRef: R, property: KProperty<*>): T
    public operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}
```

> 문법상의 규정 되어 있는 getter, setter의 구현을 외부 클래스의 인턴스의 구현을 문법상 동일하게 반영 해주는 것이다. 

생각보다 이해가 쉽진 않은 것 같다. :(

`Delegate` 를 이해하려면 결국 C#의 문법 개념을 좀 찾아 보는게 좋겠다 싶어서 찾아 봤다. 

하지만, 약간은 다른 점도 있고, 유사한 부분도 있다. 그래서 kotlin에 맞게 이해하는게 중요하다는 결론에 도달했다. 

### lateinit var 

```kotlin
class NotNull {
//    var a: String by Delegates.notNull() 아래 코드와 동일 하다. 
    lateinit var a: String
    fun action(b: String) {
        println(a)
    }
}
``` 

### decorator 

```kotlin
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
```
## immutable delegation

* 확정된 값을 나중에 사용 하고 싶다. (값은 확정 되지만, 실제 연산을 나중에 하고 싶다.) ==> lazy ? 

```kotlin
public interface Lzay<out T> {
    public val value: T
    public fun isInitialized(): Boolean
}
```

```kotlin
class Immun(override val value: String): Lazy<String> {
    override fun isInitialized() = true
}
class CustomImmun(v: String) {
    val a by Immun(v)
    fun action(v: String) {
        println(a)
    }
}
```

## map delegatation

```kotlin
class MapDele(var dele: MutableMap<String, Any?>) {
    val a: String by dele
    val b: Int by dele
}

val md = MapDele(mutableMapOf("a" to "ABC", "b" to 3))
println("md - ${md.a}")
println("md - ${md.b}")
md.dele = mutableMapOf("a" to "abc", "b" to 7)
println("md - ${md.a}")
println("md - ${md.b}")
```

## class delegation


### has a 

```kotlin
interface Mobile {
    fun move(): String
    fun stop(): String
}

class Car(val name: String): Mobile {
    override fun move() = "$name 이동"
    override fun stop() = "$name 정지"
}

class SportsCar(val car: Car): Mobile {
    override fun move() = car.move()
    override fun stop() = car.stop()
    fun highSpeed() = "고속이동"
}
```

`SportsCar` 객체는 실제 `move(), stop()`의 행위를 외부에 위임하고, 행위를 위임한 객체를 소유만 한다. 
하지만, 이런 객체류는 굉장히 무거운 패턴에 해당 한다. 

위의 모델이 바로 소유 모델이다. 행위를 약속 해두면, 핵심을 고치지 않고도 확장이 가능하다는 것이 핵심이다. 
 
 
```kotlin
class FastCar(car: Car): Mobile by car {
    fun fastSpeed() = "빠른 이동"
}
```

`kotlin에서는` 아예 문법적으로 구성을 해버린다. `Mobile`의 기능을 `by car`에 위임을 하고, `fastSpeed()`의 행위만 담당하게 만들고, 불필요한 정보를 빼버리도록 만든다.  

class delegation은 결국 instance가 생성 될 때, 다른 class의 instance한테 위임 하는 것이다.

 
 ```kotlin
class UltraCar(var car: Car): Mobile by car {
    fun UltraSpeed() = "초빠른 이동"
}

val ucar = UltraCar(Car("택시"))
println("ucar - ${ucar.move()}")
println("ucar - ${ucar.stop()}")
println("ucar - ${ucar.UltraSpeed()}")
ucar = Car("야간버스")
println("ucar - ${ucar.move()}")
```

본래 의도라면, 택시에서 야간 버스로 바뀌게 되면, 1차 참조기 때문에 안바뀌어야 할 것 같은데, `double dispatch`의 `pointer`를 통해 전략 패턴을 아예 바꿔 버린다. 
전략 객체를 바꾸면 바로 반영이 된다는 특징을 가지고 이 부분이 강력 하다. 

```kotlin
class DogCar: Mobile by object: Mobile {
    val name = "아인"
    override fun move() = "$name run"
    override fun stop() = "$name stop" 
} {
    fun fastSpeed() = "fast run"
} 
val dcar = DogCar()
println("dcar - ${dcar.move()}")
println("dcar - ${dcar.stop()}")
println("dcar - ${dcar.fastSpeed()}")
```

위 처럼 선언 하게 되면, 인스턴스를 매번 생성 하게 된다. 

## by by by

```kotlin
interface AA{fun a()}
interface BB{fun b()}

class AB0:AA by object:AA {
    override fun a() {
     // a
    }
}, BB by object:BB {
     override fun b() {
        // b    
    }
}{      
    // AB0
}

```

`object는` 한 번에 하나의 인터페이스만 담당 한다. 

```kotlin
class AB1(v: AA = object: AA {
    override fun a() {
        // a
    }   
}): AA by v {
    // AB1
}

// decorator pattern
class AB2:AA by object:AA by object: AA {
    override fun a() {
        //a 
    }
} { 
    // object
} {
    // AB2
}
```

`by chain`을 이용해서 `decorator chain`을 만들 수 있다. 
