package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class WinningNumbersTest : DescribeSpec({
    describe("WinningNumbers") {
        context("생성이 될 때") {

            it("정상적인 6개의 번호들과 중복되지 않은 하나의 보너스 번호로 생성이 된다.") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

            }
        }
        context("번호들의 갯수가 6개가 아니라면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5), 7)
                }
            }
        }

        context("번호들이 숫자의 범위(1~45)를 벗어나면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 46), 7)
                }
            }
        }

        context("번호들이 중복된 숫자가 존재 하면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 5), 7)
                }
            }
        }

        context("보너스 번호가 숫자의 범위(1~45)를 벗어나면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 0)
                }
            }
        }

        context("당첨번호와 보너스번호가 중복된다면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 7), 7)
                }
            }
        }
    }

    describe("match 메소드") {
        val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

        context("로또와 비교할 때") {

            it("당첨번호와 6개 일치하면 1등을 반환한다") {
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))

                winningNumbers.match(lotto) shouldBe Rank.FIRST
            }

            it("당첨번호와 5개와 보너스 번호가 일치하면 2등을 반환한다") {
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 7))

                winningNumbers.match(lotto) shouldBe Rank.SECOND
            }

            it("당첨번호와 5개만 일치하면 3등을 반환한다") {
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 16))

                winningNumbers.match(lotto) shouldBe Rank.THIRD
            }

            it("당첨번호와 4개 일치하면 4등을 반환한다") {
                val lotto = Lotto(listOf(1, 2, 3, 4, 15, 16))

                winningNumbers.match(lotto) shouldBe Rank.FOURTH
            }

            it("당첨번호와 3개 일치하면 5등을 반환한다") {
                val lotto = Lotto(listOf(1, 2, 3, 14, 15, 16))

                winningNumbers.match(lotto) shouldBe Rank.FIFTH
            }

            it("모두 해당하지 않는 경우 NONE을 반환한다") {
                val lotto1 = Lotto(listOf(1, 2, 13, 14, 15, 16))
                val lotto2 = Lotto(listOf(1, 12, 13, 14, 15, 16))
                val lotto3 = Lotto(listOf(11, 12, 13, 14, 15, 16))

                winningNumbers.match(lotto1) shouldBe Rank.NONE
                winningNumbers.match(lotto2) shouldBe Rank.NONE
                winningNumbers.match(lotto3) shouldBe Rank.NONE
            }


        }
    }
})