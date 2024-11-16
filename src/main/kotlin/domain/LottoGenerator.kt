package domain

class LottoGenerator {

    companion object {
        val LOTTO_NUMBER_RANGE = 1..45
        val LOTTO_NUMBER_COUNT = 6

        fun generate() : List<Int>{
            return LOTTO_NUMBER_RANGE.shuffled().take(LOTTO_NUMBER_COUNT)
        }
    }
}