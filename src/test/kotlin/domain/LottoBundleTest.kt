package domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class LottoBundleTest : DescribeSpec({
    describe("LottoBundle") {

        context("생성이 될 때") {
            val lotto1 = Lotto(listOf(1, 2, 3, 4, 5, 6))
            val lotto2 = Lotto(listOf(11, 12, 13, 14, 15, 16))
            val lotto3 = Lotto(listOf(21, 22, 23, 24, 25, 26))
            val lotto4 = Lotto(listOf(31, 32, 33, 34, 35, 36))
            val lotto5 = Lotto(listOf(40, 41, 42, 43, 44, 45))

            it("여러 로또들의 모음으로 생성이 된다") {
                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3, lotto4, lotto5))
            }
            it("한 개인 경우에도 생성이 가능하다") {
                val lottoBundle = LottoBundle(listOf(lotto1))
            }
        }
    }

    describe("checkRanks 메소드") {
        val lotto1 = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val lotto2 = Lotto(listOf(1, 2, 3, 4, 5, 7))
        val lotto3 = Lotto(listOf(1, 2, 3, 4, 5, 23))
        val lotto4 = Lotto(listOf(1, 2, 3, 4, 5, 43))

        val winningLotto = Lotto(listOf(1, 2, 3, 4, 5, 6))
        val bonusNumber = 7

        context("당첨 번호와 비교 할 때") {

        }

        it("1등과 2등이 각각 하나 씩 있는 경우"){
            val lottoBundle = LottoBundle(listOf(lotto1, lotto2))
            val result = lottoBundle.checkRanks(winningLotto, bonusNumber)

            result[Rank.FIRST] shouldBe 1
            result[Rank.SECOND] shouldBe 1
        }

        it("3등이 여러 개 존재 하는 경우"){
            val lottoBundle = LottoBundle(listOf(lotto3, lotto4))
            val result = lottoBundle.checkRanks(winningLotto, bonusNumber)

            result[Rank.THIRD] shouldBe 2
        }

        it("모두 낙첨이 되는 경우"){
            val fail1 = Lotto(listOf(21, 22, 23, 24, 25, 26))
            val fail2 = Lotto(listOf(21, 22, 23, 24, 25, 36))
            val fail3 = Lotto(listOf(21, 22, 23, 24, 25, 45))

            val lottoBundle = LottoBundle(listOf(fail1, fail2, fail3))
            val result = lottoBundle.checkRanks(winningLotto, bonusNumber)

            result[Rank.NONE] shouldBe 3
        }

    }
})