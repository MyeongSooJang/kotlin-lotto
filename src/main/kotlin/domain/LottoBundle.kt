package domain

import domain.Rank.Companion.findRank

class LottoBundle(val lottos: List<Lotto>) {

    fun checkRanks(winningNumbers: Lotto, bonusNumber: Int): Map<Rank, Int> {
        val rankCounts = mutableMapOf<Rank, Int>()
        for (lotto in lottos) {
            val matchCount = lotto.matchCount(winningNumbers)
            val matchBonus = if (matchCount == 5) {
                lotto.containsBonusNumber(bonusNumber)
            } else {
                false
            }
            val rank = findRank(matchCount, matchBonus)
            rankCounts[rank] = rankCounts.getOrDefault(rank, 0) + 1
        }
        return rankCounts
    }


}