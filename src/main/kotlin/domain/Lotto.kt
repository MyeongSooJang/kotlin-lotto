package domain

class Lotto(numbers : List<Int>) {
    val lottoNumbers : List<Int>

    init {
        require(numbers.size == LOTTO_COUNT) {
            "로또 번호는 ${LOTTO_COUNT}개여야 합니다."
        }
        require(numbers.all { it in NUMBER_RANGE }) {
            "로또 번호는 ${NUMBER_RANGE.first} 부터 ${NUMBER_RANGE.last}이여야 합니다."
        }
        require(numbers.distinct().size == numbers.size) {
            "로또 번호는 중복이 불가능 합니다."
        }

        this.lottoNumbers = numbers.sorted()
    }

    companion object {
        private val NUMBER_RANGE = 1..45
        private const val LOTTO_COUNT = 6
    }

}



