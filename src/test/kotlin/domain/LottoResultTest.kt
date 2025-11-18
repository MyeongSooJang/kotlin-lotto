package domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LottoResultTest : DescribeSpec({
    describe("LottoResult") {

        context("getTotalPrize()") {

            it("1등 1개일 때 총 당첨금은 2,000,000,000원이다") {
                val bundleResult = mapOf(
                    Rank.FIRST to 1
                )
                val lottoResult = LottoResult(bundleResult)

                lottoResult.getTotalPrize() shouldBe 2_000_000_000L
            }

            it("3등 2개, 5등 3개일 때 총 당첨금은 3,015,000원이다") {
                val bundleResult = mapOf(
                    Rank.THIRD to 2,
                    Rank.FIFTH to 3
                )
                val lottoResult = LottoResult(bundleResult)

                lottoResult.getTotalPrize() shouldBe 3_015_000L
            }

            it("모든 등수가 섞여 있을 때 총합을 계산한다") {
                val bundleResult = mapOf(
                    Rank.FIRST to 1,
                    Rank.SECOND to 1,
                    Rank.THIRD to 2,
                    Rank.FORTH to 3,
                    Rank.FIFTH to 5
                )
                val lottoResult = LottoResult(bundleResult)

                lottoResult.getTotalPrize() shouldBe 2_033_175_000L
            }

            it("당첨이 없을 때 총 당첨금은 0원이다") {
                val bundleResult = mapOf(
                    Rank.NONE to 3
                )
                val lottoResult = LottoResult(bundleResult)

                lottoResult.getTotalPrize() shouldBe 0L
            }

            it("빈 결과일 때 총 당첨금은 0원이다") {
                val bundleResult = emptyMap<Rank, Int>()
                val lottoResult = LottoResult(bundleResult)

                lottoResult.getTotalPrize() shouldBe 0L
            }

        }

        context("calculateProfitRate()") {

            it("10,000원 투자, 5,000원 당첨 시 수익률은 50%이다") {
                val bundleResult = mapOf(
                    Rank.FIFTH to 1
                )
                val lottoResult = LottoResult(bundleResult)

                val profitRate = lottoResult.calculateProfitRate(10_000L)
                profitRate shouldBe 50.0
            }

            it("10,000원 투자, 15,000원 당첨 시 수익률은 150%이다") {
                val bundleResult = mapOf(
                    Rank.FIFTH to 3
                )
                val lottoResult = LottoResult(bundleResult)

                val profitRate = lottoResult.calculateProfitRate(10_000L)
                profitRate shouldBe 150.0
            }

            it("10,000원 투자, 10,000원 당첨 시 수익률은 100%이다 (본전)") {
                val bundleResult = mapOf(
                    Rank.FIFTH to 2
                )
                val lottoResult = LottoResult(bundleResult)

                val profitRate = lottoResult.calculateProfitRate(10_000L)
                profitRate shouldBe 100.0
            }

            it("100,000원 투자, 당첨 없을 때 수익률은 0%이다") {
                val bundleResult = mapOf(
                    Rank.NONE to 100
                )
                val lottoResult = LottoResult(bundleResult)

                val profitRate = lottoResult.calculateProfitRate(100_000L)
                profitRate shouldBe 0.0
            }

            it("10,000원 투자, 65,000원 당첨 시 수익률은 650%이다") {
                val bundleResult = mapOf(
                    Rank.FIFTH to 3,
                    Rank.FORTH to 1
                )
                val lottoResult = LottoResult(bundleResult)

                val profitRate = lottoResult.calculateProfitRate(10_000L)
                profitRate shouldBe 650.0
            }

        }

    }
})