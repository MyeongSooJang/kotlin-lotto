package domain

class LottoResult(val bundleResult: Map<Rank, Int>) {

    fun printBundleResult() {
        println("=== 최종 결과를 출력합니다 ===")
        Rank.entries.forEach { rank ->
            val count = bundleResult.getOrDefault(rank, 0)
            println("${rank.name} : $count")
        }
    }

    fun calculateProfitRate(purchaseAmount: Long): Double {
        val totalPrize = bundleResult.entries.sumOf { (rank, count) -> rank.prize * count }
        return (totalPrize.toDouble() / purchaseAmount) * 100
    }

    fun printProfitRate(purchaseAmount: Long) {
        val rate = calculateProfitRate(purchaseAmount)
        println("총 수익률은 %.2f%%".format(rate))

    }
}