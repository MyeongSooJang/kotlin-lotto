package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class PurchaseTypeTest : DescribeSpec({
    describe("PurchaseType") {

        context("from 메서드 - 정상 케이스") {

            it("1을 입력하면 AUTO를 반환한다") {
                val result = PurchaseType.from(1)
                result shouldBe PurchaseType.AUTO
            }

            it("2를 입력하면 MANUAL을 반환한다") {
                val result = PurchaseType.from(2)
                result shouldBe PurchaseType.MANUAL
            }

        }

        context("from 메서드 - 예외 케이스") {

            it("0을 입력하면 NoSuchElementException을 던진다") {
                val exception = shouldThrow<NoSuchElementException> {
                    PurchaseType.from(0)
                }
                exception.message shouldContain "1과 2중에서 입력하세요"
            }

            it("3을 입력하면 NoSuchElementException을 던진다") {
                val exception = shouldThrow<NoSuchElementException> {
                    PurchaseType.from(3)
                }
                exception.message shouldContain "1과 2중에서 입력하세요"
            }

            it("음수를 입력하면 NoSuchElementException을 던진다") {
                val exception = shouldThrow<NoSuchElementException> {
                    PurchaseType.from(-1)
                }
                exception.message shouldContain "1과 2중에서 입력하세요"
            }

            it("매우 큰 숫자를 입력하면 NoSuchElementException을 던진다") {
                val exception = shouldThrow<NoSuchElementException> {
                    PurchaseType.from(100)
                }
                exception.message shouldContain "1과 2중에서 입력하세요"
            }
        }

        context("각 타입의 value 속성") {

            it("AUTO의 value는 1이다") {
                PurchaseType.AUTO.value shouldBe 1
            }

            it("MANUAL의 value는 2이다") {
                PurchaseType.MANUAL.value shouldBe 2
            }
        }

        context("Enum entries") {

            it("PurchaseType은 2개의 값을 가진다") {
                PurchaseType.entries.size shouldBe 2
            }

            it("entries에 AUTO와 MANUAL이 포함된다") {
                PurchaseType.entries shouldBe listOf(PurchaseType.AUTO, PurchaseType.MANUAL)
            }
        }
    }
})
