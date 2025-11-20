package domain

data class Lotto(val lottoNumbers: List<Int>) {
    init {
        require(lottoNumbers.size == LottoConstant.LOTTO_COUNT) {
            "로또 번호는 ${LottoConstant.LOTTO_COUNT}개여야 합니다."
        }
        require(lottoNumbers.all { it in LottoConstant.LOTTO_RANGE }) {
            "로또 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 ${LottoConstant.LOTTO_RANGE.last}까지여야 합니다."
        }
        require(lottoNumbers.distinct().size == lottoNumbers.size) {
            "로또 번호는 중복이 불가능 합니다."
        }
    }
}



