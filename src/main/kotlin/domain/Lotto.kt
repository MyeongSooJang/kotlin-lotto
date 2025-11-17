package domain

class Lotto(numbers: List<Int>) {
    val lottoNumbers: List<Int> = numbers.sorted()

    init {
        require(numbers.size == LottoConstant.LOTTO_COUNT) {
            "로또 번호는 ${LottoConstant.LOTTO_COUNT}개여야 합니다."
        }
        require(numbers.all { it in LottoConstant.LOTTO_RANGE }) {
            "로또 번호는 ${LottoConstant.LOTTO_RANGE.first} 부터 ${LottoConstant.LOTTO_RANGE.last}이여야 합니다."
        }
        require(numbers.distinct().size == numbers.size) {
            "로또 번호는 중복이 불가능 합니다."
        }


    }


}



