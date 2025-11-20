package view
import domain.LottoBundle
import domain.LottoResult
import domain.Money
import domain.Rank

class OutputView {
    fun showLottoTotalPrice(lottoTotalPrice: Money){
        println("로또 총 금액은 ${lottoTotalPrice.amount}원 입니다")
    }

    fun showChange(change: Money) {
        println("거스름돈은 ${change.amount}원입니다")
    }

    fun showPurchasedLottos(lottoBundle: LottoBundle) {
        println("\n=== 구매한 로또 번호 ===")
        lottoBundle.getLotto().forEachIndexed { index, lotto ->
            println("${index + 1}번째 로또: ${lotto.lottoNumbers}")
        }
        println()
    }

    fun showWinningStatistics(lottoResult: LottoResult) {
        println("\n=== 당첨 통계 ===")
        Rank.entries
            .filter { it != Rank.NONE }
            .reversed()
            .forEach { rank ->
                val count = lottoResult.bundleResult.getOrDefault(rank, 0)
                showMatchResult(rank, count)
            }
    }

    private fun showMatchResult(rank: Rank, count: Int) {
        val matchInfo = when (rank) {
            Rank.FIRST -> "${rank.matchCount}개 일치"
            Rank.SECOND -> "${rank.matchCount}개 일치, 보너스 번호 일치"
            else -> "${rank.matchCount}개 일치"
        }
        val prizeFormatted = String.format("%,d", rank.prize)
        println("$matchInfo (${prizeFormatted}원) - ${count}개")
    }

    fun showProfitRate(profitRate: Double) {
        println("\n총 수익률은 %.2f%%입니다.".format(profitRate))
    }

    fun showTotalPrize(totalPrize: Long) {
        val prizeFormatted = String.format("%,d", totalPrize)
        println("총 당첨 금액은 ${prizeFormatted}원입니다.")
    }

    fun showFinalResult(lottoResult: LottoResult, purchaseAmount: Long) {
        showWinningStatistics(lottoResult)

        val totalPrize = lottoResult.totalPrize
        showTotalPrize(totalPrize)

        val profitRate = lottoResult.calculateProfitRate(purchaseAmount)
        showProfitRate(profitRate)
    }


}