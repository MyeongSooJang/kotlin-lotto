package domain

enum class Rank(val matchCount: Int,
                val matchBonusNumber : Boolean,
                val prize : Long) {

    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FORTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    companion object {
        fun findRank(matchCount: Int, matchBonusNumber : Boolean) : Rank {
            return entries
                .find{it.matchCount == matchCount && it.matchBonusNumber == matchBonusNumber} ?:Rank.NONE
        }
    }

    fun getPrize() : Long {
        return prize
    }


}