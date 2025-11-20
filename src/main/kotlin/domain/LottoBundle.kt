package domain


class LottoBundle(private val lottos: List<Lotto>) {

    fun checkRanks(winningNumbers: WinningNumbers): Map<Rank, Int> =
         lottos.map(winningNumbers::match)
            .groupingBy { it }
            .eachCount()


    fun getLotto() : List<Lotto> = lottos
}
