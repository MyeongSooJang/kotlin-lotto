# 🎰 로또 기계 (Kotlin Lotto)

## 📝 프로젝트 소개

**Kotlin을 이용하여 로또 머신의 로직을 구현한 프로젝트입니다.**

이 프로젝트는 실제 로또 게임의 흐름을 따라 사용자가 로또를 구매하고, 당첨 번호를 확인하여 수상 여부를 판정하는 시스템을 구현합니다.

### 개발 목표
- 객체지향 설계 원칙을 따른 유지보수 가능한 코드 작성
- 도메인 주도 설계(DDD)로 비즈니스 로직 분리
- 철저한 단위 테스트로 코드 품질 보장
- Kotlin의 고급 기능 활용 (Data class, Sealed class, Value class 등)

---

## ✨ 주요 기능

### 1. 로또 구매 관리
- **자동 번호 생성** : 1~45 범위에서 중복 없는 6개 번호 자동 생성
- **수동 번호 생성** : 사용자가 직접 입력한 6개 번호로 로또 생성
- **구매 개수 선택** : 1~100장까지 구매 가능
- **거스름돈 계산** : 지불액과 총 가격 비교로 정확한 거스름돈 계산

### 2. 입력값 유효성 검증
- **번호 범위 검증** : 1~45 범위만 허용
- **중복 검증** : 6개 번호의 중복 확인
- **개수 검증** : 반드시 6개의 번호만 입력
- **금액 검증** : 구매 가능한 금액 범위 확인

### 3. 당첨 판정 및 통계
- **당첨 번호 비교** : 당첨 번호와 보너스 번호를 통한 정확한 매칭
- **등수 판정** : 일치 개수와 보너스 번호 여부로 등수 자동 판정
  - 1등: 6개 일치 (2,000,000,000원)
  - 2등: 5개 일치 + 보너스 (30,000,000원)
  - 3등: 5개 일치 (1,500,000원)
  - 4등: 4개 일치 (50,000원)
  - 5등: 3개 일치 (5,000원)
  - 꽝: 2개 이하 일치

### 4. 결과 분석
- **당첨금 계산** : 모든 로또의 당첨금 총합 계산
- **수익률 분석** : 투자 금액 대비 수익률 계산 및 표시
- **상세 통계** : 등수별 당첨 개수 및 금액 조회

---

## 📂 프로젝트 구조 및 설계

```
src/
├── main/kotlin/
│   ├── domain/               # 📌 도메인 계층 - 비즈니스 로직
│   │   ├── Lotto.kt         # 로또 번호를 나타내는 도메인 모델
│   │   ├── WinningNumbers.kt # 당첨 번호와 보너스 번호 관리
│   │   ├── Rank.kt          # 로또 등수를 나타내는 Enum
│   │   ├── LottoBundle.kt   # 여러 로또를 관리하는 일급 컬렉션
│   │   ├── LottoGenerator.kt # 로또 번호 생성 로직
│   │   ├── LottoMachine.kt   # 로또 머신의 전체 흐름 제어
│   │   ├── LottoResult.kt    # 게임 결과 데이터
│   │   ├── ValueTypes.kt     # Money, Count 값 타입
│   │   └── ...
│   ├── service/              # 🔧 서비스 계층 - 비즈니스 로직 조정
│   │   └── LottoService.kt   # 게임 플로우 조정
│   ├── controller/           # 🎮 제어 계층 - 입출력 제어
│   │   └── LottoController.kt # 사용자 입출력 중앙 제어
│   ├── view/                 # 👁️ 뷰 계층 - 사용자 입출력
│   │   ├── InputView.kt      # 사용자 입력 관리
│   │   └── OutputView.kt     # 게임 결과 출력
│   ├── dto/                  # 📦 데이터 전송 객체
│   │   ├── GameData.kt
│   │   ├── GameResult.kt
│   │   └── PurchaseData.kt
│   ├── Extension.kt          # 🔗 확장 함수들 (입력값 검증 및 변환)
│   └── LottoApplication.kt   # 🚀 메인 진입점
│
└── test/kotlin/              # ✅ 테스트 계층
    ├── domain/               # 도메인 로직 테스트 (약 1,200줄)
    └── controller/           # 통합 테스트
```

### 아키텍처의 특징

**계층화 아키텍처 (Layered Architecture)**
```
User Input → Controller → Service → Domain → Database
                            ↓
                         OutputView
```

1. **Controller**: 입출력 제어
2. **Service**: 비즈니스 로직 조정
3. **Domain**: 핵심 비즈니스 로직
4. **View**: 사용자 입출력

---

## 🛠️ 핵심 설계 패턴 및 기법

### 1. 일급 컬렉션 (First-Class Collection)

**LottoBundle.kt**
```kotlin
class LottoBundle(private val lottos: List<Lotto>) {
    fun checkRanks(winningNumbers: WinningNumbers): Map<Rank, Int> =
        lottos.map { winningNumbers.match(it) }
            .groupingBy { it }
            .eachCount()
}
```

**장점:**
- 컬렉션에 대한 비즈니스 로직을 캡슐화
- 리스트 직접 접근 방지
- 의도가 명확한 메서드명

### 2. 값 객체 (Value Object) - @JvmInline

**ValueTypes.kt**
```kotlin
@JvmInline
value class Money(val amount: Long) {
    init {
        require(amount >= 0) { "금액은 음수가 될 수 없습니다" }
    }

    operator fun plus(other: Money) = Money(amount + other.amount)
    operator fun times(count: Count) = Money(amount * count.value)
}

@JvmInline
value class Count(value: Int) {
    init {
        require(value in 1..100) { "1~100장까지 구매 가능합니다" }
    }
}
```

**장점:**
- 타입 안전성: Long 대신 Money 사용으로 의도 명확화
- 성능 최적화: @JvmInline으로 런타임 래퍼 제거
- 불변성 보장: init 블록에서 유효성 검증
- 연산자 오버로딩으로 직관적인 코드

### 3. Data Class

**GameResult.kt**
```kotlin
data class GameResult(
    val winningNumbers: WinningNumbers,
    val bonusNumber: Int,
    val matchResults: Map<Rank, Int>
) {
    val totalPrize: Long
        get() = matchResults.entries.sumOf { (rank, count) -> rank.prize * count }
}
```

**장점:**
- 자동 생성: equals(), hashCode(), toString(), copy()
- 보일러플레이트 코드 제거
- 불변성 유지

### 4. Enum을 이용한 도메인 모델링

**Rank.kt**
```kotlin
enum class Rank(
    val matchCount: Int,
    val matchBonusNumber: Boolean,
    val prize: Long
) {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    companion object {
        fun findRank(matchCount: Int, matchBonusNumber: Boolean): Rank =
            entries.find { it.matchCount == matchCount && it.matchBonusNumber == matchBonusNumber } ?: NONE
    }
}
```

**장점:**
- 정해진 6가지 등수만 존재
- 잘못된 등수 생성 불가능 (컴파일 타임에 검증)
- when 표현식에서 모든 케이스 처리 강제

### 5. Sealed Class를 이용한 함수형 에러 처리

**Result.kt**
```kotlin
sealed interface Result<T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure<T>(val error: String) : Result<T>
}
```

**사용 예시:**
```kotlin
fun processPurchase(payment: Money): Result<PurchaseData> {
    return if (payment.amount >= totalPrice) {
        Result.Success(purchaseData)
    } else {
        Result.Failure("금액이 부족합니다")
    }
}

// 사용할 때
when (val result = processPurchase(payment)) {
    is Result.Success -> println("구매 성공: ${result.value}")
    is Result.Failure -> println("에러: ${result.error}")
}
```

**장점:**
- null 사용 제거
- Exception 없이 명시적 에러 처리
- 타입 안전성

### 6. 확장 함수를 이용한 입력 검증

**Extension.kt**
```kotlin
fun String.toLottoNumbers(): Result<List<Int>> {
    return try {
        val numbers = this.split(",").map { it.trim().toInt() }

        require(numbers.size == 6) { "번호는 6개여야 합니다" }
        require(numbers.all { it in 1..45 }) { "1~45 범위의 번호만 입력 가능합니다" }
        require(numbers.distinct().size == 6) { "중복된 번호가 있습니다" }

        Result.Success(numbers.sorted())
    } catch (e: Exception) {
        Result.Failure(e.message ?: "입력 형식이 잘못되었습니다")
    }
}
```

**장점:**
- 검증 로직의 재사용성
- 입력 계층과 도메인 로직 분리
- 명확한 에러 메시지

---

## ✅ 테스트 전략

### 테스트 프레임워크
- **Kotest 5.8.0**: BDD 스타일의 테스트 프레임워크
- **JUnit 5**: 테스트 러너

### 테스트 커버리지

| 계층 | 테스트 파일 | 라인 수 | 주요 테스트 |
|------|----------|--------|----------|
| Domain | LottoTest.kt | 117 | 로또 생성, 유효성 검증 (18개) |
| Domain | WinningNumbersTest.kt | 325 | 당첨 번호 비교, 등수 판정 (50+개) |
| Domain | RankTest.kt | 213 | 모든 등수 속성, findRank (40개) |
| Domain | LottoGeneratorTest.kt | 72 | 자동/수동 생성 |
| Domain | LottoBundleTest.kt | - | 일급 컬렉션 동작 |
| Domain | ValueTypesTest.kt | - | Money, Count 검증 |
| Controller | LottoApplicationTest.kt | - | 통합 테스트 |
| **합계** | **총 10+개** | **약 1,200줄** | **100+개 테스트** |

### 테스트 예시

```kotlin
describe("Lotto") {
    context("로또 생성") {
        it("6개의 서로 다른 번호로 로또를 생성한다") {
            val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
            lotto.lottoNumbers shouldHaveSize 6
        }

        it("로또 번호는 1~45 범위여야 한다") {
            shouldThrow<IllegalArgumentException> {
                Lotto(listOf(0, 1, 2, 3, 4, 5))  // 0은 범위 초과
            }
        }

        it("중복된 번호가 있으면 예외를 발생시킨다") {
            shouldThrow<IllegalArgumentException> {
                Lotto(listOf(1, 1, 2, 3, 4, 5))  // 1이 중복
            }
        }
    }
}
```

**테스트의 특징:**
- ✅ BDD 스타일로 가독성 높음
- ✅ describe-context-it 구조로 테스트 의도 명확화
- ✅ 정상 케이스와 예외 케이스 모두 검증
- ✅ 경계값 테스트 포함

---

## 🚀 실행 방법

### 필수 요구사항
- Java 21 이상
- Kotlin 2.2.0

### 빌드 및 실행
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew run

# 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests LottoTest
```

### 게임 플레이 흐름
1. 구매 유형 선택 (자동/수동)
2. 구매 개수 입력 (1~100장)
3. 금액 입력
4. 로또 번호 생성 또는 입력
5. 당첨 번호 입력
6. 보너스 번호 입력
7. 당첨 결과 확인 및 수익률 분석

---

## 📊 주요 코드 메트릭

- **총 라인 수**: 약 2,400줄
- **메인 코드**: 약 1,200줄
- **테스트 코드**: 약 1,200줄
- **테스트 케이스**: 100+ 개
- **주요 클래스**: 20+ 개
- **테스트 커버리지**: 도메인 중심

---

## 💡 학습 포인트

### Kotlin 고급 기능 활용
- ✅ Data class와 자동 생성 메서드
- ✅ Enum과 Sealed class의 타입 안전성
- ✅ @JvmInline value class를 이용한 성능 최적화
- ✅ 확장 함수 (Extension Functions)
- ✅ 고차 함수와 함수형 프로그래밍

### 설계 패턴 적용
- ✅ 계층화 아키텍처 (Layered Architecture)
- ✅ 일급 컬렉션 패턴 (First-Class Collection)
- ✅ 값 객체 패턴 (Value Object)
- ✅ 팩토리 패턴 (Factory Pattern)
- ✅ 함수형 에러 처리 (Functional Error Handling)

### 객체지향 원칙
- ✅ 단일 책임 원칙 (SRP)
- ✅ 개방-폐쇄 원칙 (OCP)
- ✅ 리스코프 치환 원칙 (LSP)
- ✅ 의존성 역전 원칙 (DIP)

### 테스트 주도 개발 (TDD)
- ✅ BDD 스타일의 명확한 테스트
- ✅ 예외 케이스와 경계값 테스트
- ✅ 높은 테스트 커버리지

---

## 🎯 프로젝트 결과

이 프로젝트를 통해:
- **로또 게임의 전체 플로우**를 구현할 수 있음
- **Kotlin의 고급 기능**을 실전에서 활용할 수 있음
- **계층화 아키텍처**를 이해하고 적용할 수 있음
- **단위 테스트와 통합 테스트**의 중요성을 인식할 수 있음
- **객체지향 설계 원칙**을 실제로 적용할 수 있음