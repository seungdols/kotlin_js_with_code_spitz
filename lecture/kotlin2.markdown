
## Base Data Type

* Kotlin에서는 Boxing, UnBoxing이 자주 일어나지 않고, 알아서 컴파일 시점에 알아서 최적화를 해준다.
  * int형일 것 같으면 알아서 발라내준다는 것!
  * 대부분, Int처럼 객체라고 생각하면 편할듯
  
* 어떤 플랫폼이든지 Kotlin에서 제공하는 데이터 타입을 사용하면 된다. 
  * `unsigned data type`도 제공한다. 
  * 플랫폼에서 지원 안할 경우 알아서 처리 한다. 
  
* 아쉬운 점은 `char 형`인데, 4byte라 utf-16 채용하는데, 이로 인해 이슈가 발생한다. 이는 Java 호환성을 위한 단점을 가진다.  
  * `Surrogate signed` 발생 하는 이슈의 해결책은 바로 `Surrogate Pair`이라고 한다. 
  * 몰라서 찾아봤는데, 이해하기 좋게 쓰여진 글을 찾았다. 
    * 참고: https://www.sysnet.pe.kr/2/0/1710
    * 참고: https://stackoverflow.com/questions/5903008/what-is-a-surrogate-pair-in-java

* `Number type`에는 모든 타입으로 형변환 할 수 있는 함수를 제공한다. 
* `Typed Array`를 제공 해준다. 다만, 플랫폼마다 100% 호환 해주지 않아서 따로 네이티브에 맞게 변환을 해준다. 
  * 대다수의 고차 함수들이 제공 된다. (기본적으로 제공한다. 함수형 함수들의 대다수 함수 제공)

* `enum`은 컴파일 타입에 확정 되는 싱글톤 인스턴스이다.
* `Any` 최상위 클래스이다. 
* `Nothing` 예외의 반환값이다. 
* `Result` 예외와 실패를 포함할 수 있는 타입
* `Unit` void처럼 반환 값이 없는 경우를 나타내는 값
* `Comparable` 비교와 순서를 결정할 수 있는 인터페이스 
* `Comparator` 실질적인 비교를 처리하는 객체
* `Function` 모든 Kotlin lambda의 부모 
* `Pair` 두 개의 값을 갖는 객체
* `Triple` 세 개의 값을 갖는 객체
* `Annotation` 애노테이션 객체
* `Lazy` 지연 객체 인터페이스
* `LazyThreadSafetyMode` 지연 모드 값 객체
* `DeprecationLevel` API등의 파기 수준을 나타내는 값 객체
* `KotlinVersion` Kotlin version 값 객체

### Throwable 

* Error 
* Exception
* Kotlin은 무조건 Nothing이 원칙이라 Catch가 의무화 되어 있지 않다.
* 그래서 둘 중, 하나의 알맞는 것을 선택하면 된다. 


## Calculator (계산기)

* tokenizer로 처리하는 것은 생각보다 힘들다. 
* 차라리 정규식으로 이용해 처리하는게 훨씬 편하다. 

* `val`: 상수 
* `var`: 변수 
* 한 줄에 하나만 할당문을 허용함. 
* kotlin에는 타입을 무조건 기입 해주어야 하는데, 유추할 수 있다면, 생략해도 된다. (Smart Casting)
  * 그래서 대다수의 언어들이 형선언이 뒤에 쓰도록 설계 되어 있다. (지우기가 쉽다.)
* 함수의 마지막 인자가 `lambda`인 경우에는 함수 괄호 밖에 써도 컴파일러가 알아서 인식한다. 
  * `map(5){it * 2}`
* `string template`은 두 가지가 존재한다. (`toString()`을 자동으로 해준다.)
  * `$name`, `${name}`
* `kotlin`에서 함수가 단일 변수에 단일 변수를 리턴하는 구조라면, `single expression function`으로 변환해서 사용할 수 있다. 
  * `type`과 변수 할당을 줄일 수 있다. 
* `?.xxxx` , `?.xxxx()`은 safe call 표현식이다. 
* 람다의 return은 감싸고 있는 함수의 return
* 인자 람다의 return의 경우는 반드시 기명 return만 가능하다.
  * 기명 람다의 경우는 `name@{return@name}`와 같이 직접 이름을 부여하는 방식이다. 
  * 자동 이름 - 람다를 인자로 받은 함수의 이름을 써주면 된다. 
    * `addEventListener("keyup") {return@addEventListener}`
