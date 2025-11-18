package domain

class LottoResult(val bundleResult: Map<Rank, Int>) {

    fun getTotalPrize(): Long {
        return bundleResult.entries.sumOf { (rank, count) -> rank.prize * count }
    }

    fun calculateProfitRate(purchaseAmount: Long): Double {
        val totalPrize = getTotalPrize()
        return (totalPrize.toDouble() / purchaseAmount) * 100
    }

}