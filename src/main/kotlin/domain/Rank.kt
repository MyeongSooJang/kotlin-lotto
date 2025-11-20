package domain

enum class Rank(val matchCount: Int,
                val matchBonusNumber : Boolean,
                val prize : Long) {

    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    NONE(0, false, 0);

    companion object {
        fun findRank(matchCount: Int, matchBonusNumber : Boolean) : Rank {
            return entries
                .find{it.matchCount == matchCount && it.matchBonusNumber == matchBonusNumber} ?:Rank.NONE
        }
    }

    fun getMatchInfo(): String = when (this) {
        FIRST -> "${matchCount}개 일치"
        SECOND -> "${matchCount}개 일치, 보너스 번호 일치"
        else -> "${matchCount}개 일치"
    }
}