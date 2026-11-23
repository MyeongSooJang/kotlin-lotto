# Kotlin Lotto

## 프로젝트 소개

우아한테크코스 프리코스 마지막 주차의 자유 주제로, Kotlin을 처음 학습하며 구현한 콘솔 로또 프로그램입니다.

3주차 미션이었던 로또 게임을 Kotlin으로 구현한 뒤, 코드 리뷰에서 "자바의 껍데기를 씌운 코드"라는 피드백을 받아 Kotlin의 언어 기능을 적용하는 방향으로 리팩토링했습니다.

## 개발 과정

커밋 이력 기준입니다.

| 기간 | 내용 |
|------|------|
| 11/15 ~ 11/18 | 도메인 구현(Lotto, LottoBundle, Rank, WinningNumbers, LottoGenerator, LottoResult)과 단위 테스트, 입출력 View 작성 |
| 11/20 | Kotlin 기능 적용 리팩토링: 입력 파싱 확장 함수, Value Class, Data Class, when 표현식, Scope Function, Destructuring, 메서드 참조, Sealed Interface 기반 Result |
| 11/21 | Controller, Service, LottoMachine 계층 분리와 DTO 패키지 추가, 테스트 보강 |
| 11/24 | 테스트 보강, 네이밍과 출력 메시지 정리 |

## 구현된 기능

### 로또 구매
- 자동 또는 수동 구매 선택
- 구매 장수 입력 (1장 ~ 100장, 1장당 1,000원)
- 결제 금액 입력 후 거스름돈 계산
- 수동 구매 시 장마다 번호 6개를 쉼표로 구분해 입력
- 자동 구매 시 1 ~ 45 범위에서 중복 없는 6개 번호를 무작위로 생성하고 정렬

### 당첨 판정
- 당첨 번호 6개와 보너스 번호 입력
- 일치 개수와 보너스 번호 일치 여부로 등수 판정

| 등수 | 조건 | 당첨금 |
|------|------|--------|
| 1등 | 6개 일치 | 2,000,000,000원 |
| 2등 | 5개 일치 + 보너스 번호 일치 | 30,000,000원 |
| 3등 | 5개 일치 | 1,500,000원 |
| 4등 | 4개 일치 | 50,000원 |
| 5등 | 3개 일치 | 5,000원 |
| 없음 | 그 외 | 0원 |

### 결과 출력
- 구매한 로또 번호 목록
- 등수별 당첨 개수와 당첨금
- 총 당첨 금액과 수익률

### 입력 검증
- `Lotto`, `WinningNumbers`의 `init` 블록에서 `require`로 번호 개수(6개), 범위(1 ~ 45), 중복, 보너스 번호 중복을 검증
- `Money`는 음수, `Count`는 1 ~ 100 범위를 벗어나면 생성 시 예외 발생
- 구매 유형, 구매 장수, 금액, 번호가 숫자 형식이 아니면 안내 메시지를 출력하고 다시 입력받음

## 프로젝트 구조

```
src/
├── main/kotlin/
│   ├── LottoApplication.kt      # main 함수 (package controller)
│   ├── Extension.kt             # String 입력 변환 확장 함수
│   ├── controller/
│   │   └── LottoController.kt   # 게임 실행과 결과 출력 호출
│   ├── service/
│   │   └── LottoService.kt      # 구매와 당첨 확인 흐름 조정
│   ├── domain/
│   │   ├── Lotto.kt             # 로또 번호 6개 (data class)
│   │   ├── LottoBundle.kt       # 로또 목록 (일급 컬렉션)
│   │   ├── WinningNumbers.kt    # 당첨 번호와 보너스 번호
│   │   ├── Rank.kt              # 등수 (enum)
│   │   ├── LottoResult.kt       # 등수별 결과, 총 당첨금, 수익률
│   │   ├── LottoGenerator.kt    # 자동, 수동 로또 생성
│   │   ├── LottoMachine.kt      # 입력 처리와 로또 생성, 당첨 판정
│   │   ├── PurchaseType.kt      # 구매 유형 (자동, 수동)
│   │   ├── LottoConstant.kt     # 로또 관련 상수
│   │   ├── ValueTypes.kt        # Money, Count (value class)
│   │   └── Result.kt            # Success, Failure (sealed interface)
│   ├── dto/
│   │   ├── PurchaseData.kt
│   │   ├── GameResult.kt
│   │   └── GameData.kt
│   └── view/
│       ├── InputView.kt
│       └── OutputView.kt
└── test/kotlin/
    ├── controller/LottoApplicationTest.kt
    └── domain/                  # 도메인 단위 테스트 9개
```

실행 흐름은 다음과 같습니다.

```
main -> LottoController -> LottoService -> LottoMachine -> domain
                  |                            |
              OutputView                   InputView
```

## 적용한 Kotlin 기능

### Value Class

`Money`와 `Count`로 금액과 장수를 구분하고, 연산자 오버로딩으로 `Count * Money`처럼 표현합니다.

```kotlin
@JvmInline
value class Money(val amount: Long) {
    init {
        require(amount >= 0) { "금액은 음수가 될수 없습니다" }
    }

    operator fun times(count: Count) = Money(amount * count.value)
    operator fun minus(other: Money) = Money(amount - other.amount)
    operator fun plus(other: Money) = Money(amount + other.amount)
    operator fun compareTo(other: Money) = amount.compareTo(other.amount)
}

@JvmInline
value class Count(val value: Int) {
    init {
        require(value in 1..100) { "1~100장까지 구매 가능합니다" }
    }

    operator fun times(price: Money) = Money(price.amount * value)
}
```

### Sealed Interface와 확장 함수를 이용한 입력 처리

입력 문자열 변환 결과를 `Result`로 표현하고, `LottoMachine`에서 `when`으로 성공과 실패를 분기합니다.

```kotlin
// Result.kt
sealed interface Result<T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure<T>(val error: String) : Result<T>
}

// Extension.kt
fun String.toPurchaseCount(): Result<Count> =
    toIntOrNull()?.let { Success(Count(it)) }
        ?: Failure("유효한 숫자를 입력해주세요")

// LottoMachine.kt
fun getPurchaseCount(): Count =
    when (val result = inputView.readPurchaseCount().toPurchaseCount()) {
        is Success -> result.value
        is Failure -> {
            println(result.error)
            getPurchaseCount()
        }
    }
```

### Data Class

`Lotto`, `WinningNumbers`, `LottoResult`와 `dto` 패키지의 클래스에 사용했습니다. `LottoResult`는 계산 프로퍼티와 구조 분해를 함께 사용합니다.

```kotlin
data class LottoResult(val bundleResult: Map<Rank, Int>) {

    val totalPrize
        get() = bundleResult.entries.sumOf { (rank, count) -> rank.prize * count }

    fun calculateProfitRate(purchaseAmount: Long): Double =
        (totalPrize.toDouble() / purchaseAmount) * 100
}
```

### 일급 컬렉션과 컬렉션 함수

```kotlin
class LottoBundle(private val lottos: List<Lotto>) {

    fun checkRanks(winningNumbers: WinningNumbers): Map<Rank, Int> =
        lottos.map(winningNumbers::match)
            .groupingBy { it }
            .eachCount()

    fun getLotto(): List<Lotto> = lottos
}
```

### Enum

등수를 6개로 제한하고, 일치 개수와 보너스 여부로 `Rank`를 찾습니다.

```kotlin
enum class Rank(val matchCount: Int, val matchBonusNumber: Boolean, val prize: Long) {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    companion object {
        fun findRank(matchCount: Int, matchBonusNumber: Boolean): Rank = /* ... */
    }
}
```

### 그 외
- Scope Function: `LottoMachine`의 `with`, `LottoService`의 `let`, `run`, `LottoApplication`의 `apply`
- Named Argument: `WinningNumbers(numbers = ..., bonusNumber = ...)`
- 메서드 참조: `winningNumbers::match`, `LottoGenerator::generate`
- `object`: `LottoConstant`
- `companion object`: `LottoGenerator`, `Rank`, `PurchaseType`

## 테스트

- 프레임워크: Kotest 5.8.0 (`DescribeSpec`), JUnit Platform
- 테스트 클래스 10개, 테스트 케이스 180개 (`it` 블록 기준)

| 테스트 클래스 | 케이스 수 |
|---------------|-----------|
| ValueTypesTest | 37 |
| WinningNumbersTest | 35 |
| RankTest | 32 |
| LottoMachineTest | 15 |
| LottoTest | 13 |
| LottoBundleTest | 11 |
| LottoResultTest | 10 |
| PurchaseTypeTest | 10 |
| LottoApplicationTest | 9 |
| LottoGeneratorTest | 8 |

`LottoMachineTest`는 `InputView`를 상속한 객체로 입력을 대체해 콘솔 입력 없이 검증합니다.

## 실행 방법

- 요구사항: Java 21, Kotlin 2.2.0 (Gradle JVM toolchain 21)
- 실행: IntelliJ에서 `LottoApplication.kt`의 `main` 함수를 실행합니다. `application` 플러그인을 설정하지 않아 `./gradlew run`은 사용할 수 없습니다.
- 테스트: `./gradlew test`
- 특정 테스트 클래스: `./gradlew test --tests "domain.LottoTest"`

## 현재 한계

- 테스트 180개 중 4개가 실패합니다.
  - `ValueTypesTest` 3개: `Money`가 0원을 허용하도록 바뀐 뒤 기존 기대값("0원이면 예외", 예외 메시지)을 수정하지 않았습니다.
  - `LottoMachineTest` 1개: 무작위로 생성한 로또가 고정 당첨 번호와 일치해 1등이 된다고 가정하는 테스트입니다.
- 숫자 형식 오류만 다시 입력받습니다. 장수 범위, 번호 개수와 중복, 구매 유형(1, 2 외 입력), 결제 금액 부족은 예외가 발생해 프로그램이 종료됩니다.
- 다시 입력받는 로직은 루프가 아닌 재귀 호출로 구현되어 있습니다.
