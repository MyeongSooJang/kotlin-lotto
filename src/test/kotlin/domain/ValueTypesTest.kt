package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ValueTypesTest : DescribeSpec({

    describe("Money") {

        context("생성") {

            it("양수 금액으로 생성된다") {
                val money = Money(1000)

                money.amount shouldBe 1000
            }

            it("최소값 1원으로 생성된다") {
                val money = Money(1)

                money.amount shouldBe 1
            }

            it("큰 금액으로 생성된다") {
                val money = Money(2_000_000_000)

                money.amount shouldBe 2_000_000_000
            }
        }

        context("생성 실패") {

            it("0원이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Money(0)
                }
                exception.message shouldContain "금액은 0보다 커야 합니다"
            }

            it("음수 금액이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Money(-1000)
                }
                exception.message shouldContain "금액은 0보다 커야 합니다"
            }

        }

        context("times 연산자 (Money * Count)") {

            it("1000원 * 5장 = 5000원") {
                val money = Money(1000)
                val count = Count(5)

                val result = money * count

                result shouldBe Money(5000)
            }

            it("1000원 * 1장 = 1000원") {
                val money = Money(1000)
                val count = Count(1)

                val result = money * count

                result shouldBe Money(1000)
            }

            it("500원 * 10장 = 5000원") {
                val money = Money(500)
                val count = Count(10)

                val result = money * count

                result shouldBe Money(5000)
            }

            it("큰 금액 계산도 정확하다") {
                val money = Money(1000)
                val count = Count(100)

                val result = money * count

                result shouldBe Money(100000)
            }
        }

        context("minus 연산자 (Money - Money)") {

            it("5000원 - 3000원 = 2000원") {
                val money1 = Money(5000)
                val money2 = Money(3000)

                val result = money1 - money2

                result shouldBe Money(2000)
            }

            it("같은 금액을 빼면 결과는 0원이 아니라 예외 (0원 불가)") {
                val money1 = Money(5000)
                val money2 = Money(5000)

                shouldThrow<IllegalArgumentException> {
                    money1 - money2
                }
            }

            it("작은 금액에서 큰 금액을 빼면 예외 (음수 불가)") {
                val money1 = Money(3000)
                val money2 = Money(5000)

                shouldThrow<IllegalArgumentException> {
                    money1 - money2
                }
            }
        }

        context("plus 연산자 (Money + Money)") {

            it("1000원 + 2000원 = 3000원") {
                val money1 = Money(1000)
                val money2 = Money(2000)

                val result = money1 + money2

                result shouldBe Money(3000)
            }

            it("같은 금액을 더한다") {
                val money1 = Money(5000)
                val money2 = Money(5000)

                val result = money1 + money2

                result shouldBe Money(10000)
            }
        }

        context("compareTo 연산자") {

            it("5000원 > 3000원") {
                val money1 = Money(5000)
                val money2 = Money(3000)

                (money1 > money2) shouldBe true
            }

            it("3000원 < 5000원") {
                val money1 = Money(3000)
                val money2 = Money(5000)

                (money1 < money2) shouldBe true
            }

            it("5000원 >= 5000원") {
                val money1 = Money(5000)
                val money2 = Money(5000)

                (money1 >= money2) shouldBe true
            }

            it("5000원 == 5000원") {
                val money1 = Money(5000)
                val money2 = Money(5000)

                (money1 == money2) shouldBe true
            }

            it("5000원 != 3000원") {
                val money1 = Money(5000)
                val money2 = Money(3000)

                (money1 != money2) shouldBe true
            }
        }
    }

    describe("Count") {

        context("생성") {

            it("1장으로 생성된다") {
                val count = Count(1)

                count.value shouldBe 1
            }

            it("50장으로 생성된다") {
                val count = Count(50)

                count.value shouldBe 50
            }

            it("최대값 100장으로 생성된다") {
                val count = Count(100)

                count.value shouldBe 100
            }
        }

        context("생성 실패") {

            it("0장이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Count(0)
                }
                exception.message shouldContain "1~100장까지 구매 가능합니다"
            }

            it("음수 장수이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Count(-1)
                }
                exception.message shouldContain "1~100장까지 구매 가능합니다"
            }

            it("101장이면 예외를 발생시킨다 (최대값 초과)") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Count(101)
                }
                exception.message shouldContain "1~100장까지 구매 가능합니다"
            }

            it("매우 큰 숫자이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Count(1000)
                }
                exception.message shouldContain "1~100장까지 구매 가능합니다"
            }
        }

        context("times 연산자 (Count * Money)") {

            it("5장 * 1000원 = 5000원") {
                val count = Count(5)
                val money = Money(1000)

                val result = count * money

                result shouldBe Money(5000)
            }

            it("1장 * 1000원 = 1000원") {
                val count = Count(1)
                val money = Money(1000)

                val result = count * money

                result shouldBe Money(1000)
            }

            it("100장 * 1000원 = 100000원") {
                val count = Count(100)
                val money = Money(1000)

                val result = count * money

                result shouldBe Money(100000)
            }
        }

        context("경계값 테스트") {

            it("최소값 1장 생성") {
                val count = Count(1)

                count.value shouldBe 1
            }

            it("최대값 100장 생성") {
                val count = Count(100)

                count.value shouldBe 100
            }

            it("최소값-1 (0장)은 실패") {
                shouldThrow<IllegalArgumentException> {
                    Count(0)
                }
            }

            it("최대값+1 (101장)은 실패") {
                shouldThrow<IllegalArgumentException> {
                    Count(101)
                }
            }
        }
    }

    describe("Money와 Count의 상호작용") {

        it("Money * Count 와 Count * Money 결과가 같다") {
            val money = Money(1000)
            val count = Count(5)

            val result1 = money * count
            val result2 = count * money

            result1 shouldBe result2
        }

        it("실제 로또 구매 시나리오: 5장 * 1000원") {
            val lottoPrice = Money(1000)
            val purchaseCount = Count(5)

            val totalPrice = purchaseCount * lottoPrice

            totalPrice shouldBe Money(5000)
        }

        it("실제 로또 구매 시나리오: 100장 구매") {
            val lottoPrice = Money(1000)
            val purchaseCount = Count(100)

            val totalPrice = lottoPrice * purchaseCount

            totalPrice shouldBe Money(100000)
        }

        it("거스름돈 계산: 10000원 지불, 5000원 사용") {
            val payment = Money(10000)
            val totalPrice = Money(5000)

            val change = payment - totalPrice

            change shouldBe Money(5000)
        }
    }
})