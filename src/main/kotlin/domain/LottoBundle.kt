package domain

import domain.Rank.Companion.findRank

class LottoBundle(val lottos: List<Lotto>) {

    fun checkRanks(winningNumbers: Lotto, bonusNumber: Int): Map<Rank, Int> {

        return lottos.map{ lotto -> calculateRank(lotto, winningNumbers,bonusNumber) }
            .groupingBy { it }
            .eachCount()
    }

    private fun calculateRank(lotto: Lotto,
                              winningNumbers: Lotto,
                              bonusNumber: Int): Rank {
        val matchCount = lotto.matchCount(winningNumbers)
        val matchBonus = matchCount == 5 && lotto.containsBonusNumber(bonusNumber)
        return findRank(matchCount, matchBonus)
    }


}