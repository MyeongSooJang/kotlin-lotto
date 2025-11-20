package domain

data class LottoResult(val bundleResult: Map<Rank, Int>) {

    val totalPrize
        get() = bundleResult.entries.sumOf { (rank, count) -> rank.prize * count }

    fun calculateProfitRate(purchaseAmount: Long): Double =
        (totalPrize.toDouble() / purchaseAmount) * 100
}