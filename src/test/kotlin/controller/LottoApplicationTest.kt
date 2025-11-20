package controller

import LottoMachine
import domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldHaveSize
import view.InputView
import view.OutputView

class LottoApplicationTest : DescribeSpec({
    describe("LottoApplication") {

        context("도메인 객체 통합 테스트") {

            it("자동 로또 생성부터 프로세스가 동작한다") {

                val lottos = List(5) { LottoGenerator.generate() }
                val lottoBundle = LottoBundle(lottos)

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults shouldNotBe null
                lottoResult shouldNotBe null
                lottoResult.totalPrize shouldNotBe null
            }

            it("수동 로또 생성부터 프로세스가 동작한다") {

                val lotto1 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 6))
                val lotto2 = LottoGenerator.generate(listOf(7, 8, 9, 10, 11, 12))
                val lotto3 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 7))

                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                val rankResults = lottoBundle.checkRanks(winningNumbers)

                rankResults[Rank.FIRST] shouldBe 1
                rankResults[Rank.SECOND] shouldBe 1
                rankResults[Rank.NONE] shouldBe 1
            }
        }

        context("실전 시나리오 통합 테스트") {

            it("시나리오 1: 5장 구매, 5등 2개 당첨") {

                val lotto1 = LottoGenerator.generate(listOf(1, 2, 3, 10, 11, 12))
                val lotto2 = LottoGenerator.generate(listOf(1, 2, 3, 13, 14, 15))
                val lotto3 = LottoGenerator.generate(listOf(10, 11, 12, 13, 14, 15))
                val lotto4 = LottoGenerator.generate(listOf(20, 21, 22, 23, 24, 25))
                val lotto5 = LottoGenerator.generate(listOf(30, 31, 32, 33, 34, 35))

                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3, lotto4, lotto5))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults[Rank.FIFTH] shouldBe 2
                rankResults[Rank.NONE] shouldBe 3

                lottoResult.totalPrize shouldBe 10_000L

                lottoResult.calculateProfitRate(5_000L) shouldBe 200.0
            }

            it("시나리오 2: 최고시나리오 1장구매, 1등 당첨") {

                val lotto1 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 6))

                val lottoBundle = LottoBundle(listOf(lotto1))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults[Rank.FIRST] shouldBe 1
                lottoResult.totalPrize shouldBe 2_000_000_000L

                val profitRate = lottoResult.calculateProfitRate(2_000L)
                profitRate shouldBe 100_000_000.0
            }

            it("시나리오 3: 2등 당첨 (5개 일치 + 보너스)") {

                val lotto1 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 7))  // 5개 + 보너스 (2등)

                val lottoBundle = LottoBundle(listOf(lotto1))


                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)


                rankResults[Rank.SECOND] shouldBe 1
                lottoResult.totalPrize shouldBe 30_000_000L
            }

            it("시나리오 4: 전부 낙첨") {
                val lotto1 = LottoGenerator.generate(listOf(10, 11, 12, 13, 14, 15))
                val lotto2 = LottoGenerator.generate(listOf(20, 21, 22, 23, 24, 25))
                val lotto3 = LottoGenerator.generate(listOf(30, 31, 32, 33, 34, 35))

                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3))


                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults[Rank.NONE] shouldBe 3
                lottoResult.totalPrize shouldBe 0L
                lottoResult.calculateProfitRate(3_000L) shouldBe 0.0
            }

            it("시나리오 5: 다양한 등수 혼합") {
                val lotto1 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 6))
                val lotto2 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 7))
                val lotto3 = LottoGenerator.generate(listOf(1, 2, 3, 4, 5, 10))
                val lotto4 = LottoGenerator.generate(listOf(1, 2, 3, 4, 10, 11))
                val lotto5 = LottoGenerator.generate(listOf(1, 2, 3, 10, 11, 12))
                val lotto6 = LottoGenerator.generate(listOf(10, 11, 12, 13, 14, 15))

                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3, lotto4, lotto5, lotto6))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults[Rank.FIRST] shouldBe 1
                rankResults[Rank.SECOND] shouldBe 1
                rankResults[Rank.THIRD] shouldBe 1
                rankResults[Rank.FOURTH] shouldBe 1
                rankResults[Rank.FIFTH] shouldBe 1
                rankResults[Rank.NONE] shouldBe 1

                val expectedTotal = 2_000_000_000L + 30_000_000L + 1_500_000L + 50_000L + 5_000L
                lottoResult.totalPrize shouldBe expectedTotal

                val profitRate = lottoResult.calculateProfitRate(6_000L)
                profitRate shouldBe (expectedTotal.toDouble() / 6_000.0 * 100)
            }
        }

        context("전체 플로우 시뮬레이션") {

            it("자동 구매 -> 당첨 확인 -> 결과 출력의 전체 흐름이 동작한다") {
                val purchaseType = PurchaseType.from(1)

                val count = Count(5)
                val lottos = List(count.value) { LottoGenerator.generate() }
                val lottoBundle = LottoBundle(lottos)

                val totalPrice = count * LottoConstant.LOTTO_PRICE
                totalPrice.amount shouldBe 5_000

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                val totalPrize = lottoResult.totalPrize
                val profitRate = lottoResult.calculateProfitRate(totalPrice.amount)

                totalPrize shouldNotBe null
                profitRate shouldNotBe null
            }

            it("수동 구매 -> 당첨 확인 -> 결과 출력의 전체 흐름이 동작한다") {
                val purchaseType = PurchaseType.from(2)

                val manualInputs = listOf(
                    listOf(1, 2, 3, 4, 5, 6),
                    listOf(1, 2, 3, 4, 5, 7),
                    listOf(10, 11, 12, 13, 14, 15)
                )
                val lottos = manualInputs.map { LottoGenerator.generate(it) }
                val lottoBundle = LottoBundle(lottos)

                val totalPrice = LottoConstant.LOTTO_PRICE * Count(3)

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)

                val rankResults = lottoBundle.checkRanks(winningNumbers)
                val lottoResult = LottoResult(rankResults)

                rankResults[Rank.FIRST] shouldBe 1
                rankResults[Rank.SECOND] shouldBe 1
                rankResults[Rank.NONE] shouldBe 1

                val totalPrize = lottoResult.totalPrize
                totalPrize shouldBe 2_030_000_000L

                val profitRate = lottoResult.calculateProfitRate(totalPrice.amount)
                profitRate shouldBe (2_030_000_000.0 / 3_000.0 * 100)
            }
        }
    }
})
