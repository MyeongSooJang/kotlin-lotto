package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class WinningNumbersTest : DescribeSpec({
    describe("WinningNumbers") {
        context("생성이 될 때") {

            it("정상적인 6개의 번호들과 중복되지 않은 하나의 보너스 번호로 생성이 된다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                winningNumbers.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
                winningNumbers.bonusNumber shouldBe 7
            }

            it("순서와 상관없이 생성된다") {
                val winningNumbers = WinningNumbers(listOf(45, 1, 23, 12, 34, 7), 10)

                winningNumbers.numbers shouldBe listOf(45, 1, 23, 12, 34, 7)
            }
        }

        context("번호들의 갯수가 6개가 아니라면") {

            it("5개이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5), 7)
                }
                exception.message shouldContain "당첨 번호는 6개여야 합니다"
            }

            it("7개이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6, 7), 8)
                }
                exception.message shouldContain "당첨 번호는 6개여야 합니다"
            }

            it("0개(빈 리스트)이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(emptyList(), 7)
                }
                exception.message shouldContain "당첨 번호는 6개여야 합니다"
            }
        }

        context("번호들이 숫자의 범위(1~45)를 벗어나면") {

            it("0이 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(0, 2, 3, 4, 5, 6), 7)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("46이 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 46), 7)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("음수가 포함되면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(-1, 2, 3, 4, 5, 6), 7)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }
        }

        context("번호들이 중복된 숫자가 존재 하면") {

            it("중복된 번호가 있으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 5), 7)
                }
                exception.message shouldContain "당첨 번호는 중복될 수 없습니다"
            }

            it("모든 번호가 같으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 1, 1, 1, 1, 1), 7)
                }
                exception.message shouldContain "당첨 번호는 중복될 수 없습니다"
            }
        }

        context("보너스 번호가 숫자의 범위(1~45)를 벗어나면") {

            it("0이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 0)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("46이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 46)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }

            it("음수이면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6), -10)
                }
                exception.message shouldContain "1부터 45까지여야 합니다"
            }
        }

        context("당첨번호와 보너스번호가 중복된다면") {

            it("보너스 번호가 당첨 번호 중 하나와 같으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 7), 7)
                }
                exception.message shouldContain "보너스 번호는 당첨 번호와 중복될 수 없습니다"
            }

            it("보너스 번호가 당첨 번호의 첫 번째와 같으면 예외를 발생시킨다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 1)
                }
                exception.message shouldContain "보너스 번호는 당첨 번호와 중복될 수 없습니다"
            }
        }

        context("경계값 테스트") {

            it("최소값 1부터 시작하는 당첨 번호를 생성한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                winningNumbers.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
            }

            it("최대값 45로 끝나는 당첨 번호를 생성한다") {
                val winningNumbers = WinningNumbers(listOf(40, 41, 42, 43, 44, 45), 39)

                winningNumbers.numbers shouldBe listOf(40, 41, 42, 43, 44, 45)
                winningNumbers.bonusNumber shouldBe 39
            }

            it("보너스 번호가 최소값 1이다") {
                val winningNumbers = WinningNumbers(listOf(2, 3, 4, 5, 6, 7), 1)

                winningNumbers.bonusNumber shouldBe 1
            }

            it("보너스 번호가 최대값 45이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 45)

                winningNumbers.bonusNumber shouldBe 45
            }
        }
    }

    describe("match 메소드") {

        context("1등 (6개 일치)") {

            it("당첨번호와 6개 일치하면 1등을 반환한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 6))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FIRST
            }

            it("순서가 달라도 6개가 일치하면 1등이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(6, 5, 4, 3, 2, 1))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FIRST
            }

            it("다른 숫자 조합으로 6개 일치하면 1등이다") {
                val winningNumbers = WinningNumbers(listOf(10, 20, 30, 40, 41, 42), 45)
                val lotto = Lotto(listOf(10, 20, 30, 40, 41, 42))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FIRST
            }
        }

        context("2등 (5개 일치 + 보너스)") {

            it("당첨번호와 5개와 보너스 번호가 일치하면 2등을 반환한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 7))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.SECOND
            }

            it("다른 조합으로 5개 일치하고 보너스가 일치하면 2등이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 10)
                val lotto = Lotto(listOf(2, 3, 4, 5, 6, 10))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.SECOND
            }

            it("순서가 달라도 5개 + 보너스가 일치하면 2등이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(7, 5, 4, 3, 2, 1))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.SECOND
            }
        }

        context("3등 (5개 일치)") {

            it("당첨번호와 5개만 일치하면 3등을 반환한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 3, 4, 5, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.THIRD
            }

            it("다른 조합으로 5개 일치하고 보너스가 불일치하면 3등이다") {
                val winningNumbers = WinningNumbers(listOf(10, 20, 30, 40, 41, 42), 15)
                val lotto = Lotto(listOf(10, 20, 30, 40, 41, 25))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.THIRD
            }
        }

        context("4등 (4개 일치)") {

            it("당첨번호와 4개 일치하면 4등을 반환한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 3, 4, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FOURTH
            }

            it("다른 조합으로 4개 일치하면 4등이다") {
                val winningNumbers = WinningNumbers(listOf(10, 20, 30, 40, 41, 42), 15)
                val lotto = Lotto(listOf(10, 20, 30, 40, 1, 2))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FOURTH
            }
        }

        context("5등 (3개 일치)") {

            it("당첨번호와 3개 일치하면 5등을 반환한다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 3, 14, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FIFTH
            }

            it("다른 조합으로 3개 일치하면 5등이다") {
                val winningNumbers = WinningNumbers(listOf(40, 41, 42, 43, 44, 45), 39)
                val lotto = Lotto(listOf(40, 41, 42, 1, 2, 3))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.FIFTH
            }
        }

        context("낙첨 (2개 이하 일치)") {

            it("2개 일치하면 낙첨이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 13, 14, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.NONE
            }

            it("1개 일치하면 낙첨이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 12, 13, 14, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.NONE
            }

            it("0개 일치하면 낙첨이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(11, 12, 13, 14, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.NONE
            }

            it("2개 일치하고 보너스가 일치해도 낙첨이다") {
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val lotto = Lotto(listOf(1, 2, 7, 14, 15, 16))

                val rank = winningNumbers.match(lotto)

                rank shouldBe Rank.NONE
            }
        }
    }
})