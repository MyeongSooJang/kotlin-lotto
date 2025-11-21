package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain


class LottoTest : DescribeSpec({
    describe("Lotto") {

        context("생성이 될 때") {

            it("정상적인 6개의 번호로 생성이 된다") {
                val lotto = Lotto(listOf(2, 34, 24, 27, 9, 1))

                lotto.lottoNumbers shouldHaveSize 6
                lotto.lottoNumbers shouldContainExactly listOf(2, 34, 24, 27, 9, 1)
            }

            it("순서와 상관없이 생성된다") {
                val lotto = Lotto(listOf(45, 1, 23, 12, 34, 7))

                lotto.lottoNumbers shouldHaveSize 6
                lotto.lottoNumbers shouldContainExactly listOf(45, 1, 23, 12, 34, 7)
            }
        }

        context("번호의 개수가 6개가 아니라면") {

            it("5개이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(2, 34, 24, 27, 9))
                }
                exception.message shouldContain "로또 번호는 6개여야 합니다"
            }

            it("7개이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(1, 2, 3, 4, 5, 6, 7))
                }
                exception.message shouldContain "로또 번호는 6개여야 합니다"
            }

            it("0개(빈 리스트)이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(emptyList())
                }
                exception.message shouldContain "로또 번호는 6개여야 합니다"
            }
        }

        context("숫자의 범위(1~45)를 벗어나면") {

            it("46이 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(2, 34, 24, 27, 9, 46))
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("0이 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(0, 34, 24, 27, 9, 6))
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("음수가 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(-1, 34, 24, 27, 9, 6))
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("매우 큰 숫자가 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(1, 2, 3, 4, 5, 100))
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }
        }

        context("중복된 숫자가 존재 하면") {

            it("중복된 번호가 있으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(2, 34, 24, 27, 9, 9))
                }
                exception.message shouldContain "로또 번호는 중복이 불가능 합니다"
            }

            it("모든 번호가 같으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    Lotto(listOf(1, 1, 1, 1, 1, 1))
                }
                exception.message shouldContain "로또 번호는 중복이 불가능 합니다"
            }
        }

        context("경계값 테스트") {

            it("최소값 1로 시작하는 번호로 생성된다") {
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))

                lotto.lottoNumbers shouldContainExactly listOf(1, 2, 3, 4, 5, 6)
            }

            it("최대값 45로 끝나는 번호로 생성된다") {
                val lotto = Lotto(listOf(40, 41, 42, 43, 44, 45))

                lotto.lottoNumbers shouldContainExactly listOf(40, 41, 42, 43, 44, 45)
            }
        }
    }
})