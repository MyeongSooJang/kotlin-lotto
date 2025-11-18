package domain


class LottoBundle(private val lottos: List<Lotto>) {

    fun checkRanks(winningNumbers: WinningNumbers): Map<Rank, Int> {

        return lottos.map{ winningNumbers.match(it) }
            .groupingBy { it }
            .eachCount()
    }

    fun getLotto() : List<Lotto> = lottos
}
