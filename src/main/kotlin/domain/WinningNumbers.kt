package domain

class WinningNumbers(val numbers : List<Int>, val bonusNumber : Int) {

    init{
        require(numbers.size == LottoConstant.LOTTO_COUNT) {
            "당첨 번호는 ${LottoConstant.LOTTO_COUNT}개여야 합니다."
        }
        require(numbers.all { it in LottoConstant.LOTTO_RANGE }) {
            "당첨 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 ${LottoConstant.LOTTO_RANGE.last}까지여야 합니다."
        }
        require(numbers.distinct().size == numbers.size) {
            "당첨 번호는 중복될 수 없습니다."
        }
        require(bonusNumber in LottoConstant.LOTTO_RANGE) {
            "보너스 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 ${LottoConstant.LOTTO_RANGE.last}까지여야 합니다."
        }
        require(bonusNumber !in numbers) {
            "보너스 번호는 당첨 번호와 중복될 수 없습니다."
        }
    }

    fun match(lotto : Lotto) : Rank {
        val matchCount = numbers.count { it in lotto.lottoNumbers }
        val hasBonus = lotto.lottoNumbers.contains(bonusNumber)
        return calculateRank(matchCount, hasBonus)
    }

    private fun calculateRank(matchCount : Int, hasBonus : Boolean) : Rank {
        return Rank.findRank(matchCount, hasBonus)
    }

}