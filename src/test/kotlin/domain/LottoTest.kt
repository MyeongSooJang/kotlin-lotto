package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe


class LottoTest : DescribeSpec({
    describe("Lotto") {

        context("생성이 될 때") {

            it("정상적인 6개의 번호로 생성이 된다") {
                val lotto = Lotto(listOf(2, 34, 24, 27, 9, 1))

                lotto.lottoNumbers shouldHaveSize 6
            }

            it("들어온 숫자들은 정렬이 된다") {
                val lotto = Lotto(listOf(2, 34, 24, 27, 9, 1))

                lotto.lottoNumbers shouldContainExactly listOf(1, 2, 9, 24, 27, 34)
            }
        }

        context("번호의 개수가 6개가 아니라면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    Lotto((listOf(2, 34, 24, 27, 9)))
                }
            }
        }

        context("숫자의 범위(1~45)를 벗어나면") {

            it("45를 초과하면 예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    Lotto((listOf(2, 34, 24, 27, 9, 46)))
                }
            }

            it("1미만이면 예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    Lotto((listOf(0, 34, 24, 27, 9, 46)))
                }
            }
        }

        context("중복된 숫자가 존재 하면") {

            it("예외를 발생시킨다") {
                shouldThrow<IllegalArgumentException> {
                    Lotto((listOf(2, 34, 24, 27, 9, 9)))
                }
            }
        }

        describe("matchCount 메소드") {

            val lotto = Lotto(listOf(2, 34, 24, 27, 9, 1))

            context("당첨 번호와 비교할 때") {

                it("6개 일치하면 6을 반환한다") {

                    lotto.matchCount(Lotto(listOf(2, 34, 24, 27, 9, 1))) shouldBe 6
                }

                it("4개 일치하면 4를 반환한다.") {
                    lotto.matchCount(Lotto(listOf(1, 2, 24, 27, 44, 45))) shouldBe 4
                }

                it("2개 일치하면 2를 반환한다.") {
                    lotto.matchCount(Lotto(listOf(1, 2, 21, 22, 44, 45))) shouldBe 2
                }
            }
        }

        describe("containsBonusNumber") {

            val lotto = Lotto(listOf(2, 34, 24, 27, 9, 1))

            context("보너스 번호와 비교할때") {

                it("보너스 번호와 일치하면 true를 반환한다") {
                    lotto.containsBonusNumber(2) shouldBe true
                }

                it("보너스와 일치하지 않으면 false를 반환한다") {
                    lotto.containsBonusNumber(3) shouldBe false
                }
            }
        }

    }


})