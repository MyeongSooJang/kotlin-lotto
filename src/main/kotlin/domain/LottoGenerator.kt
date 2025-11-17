package domain

class LottoGenerator {

    companion object {
        fun generate(): Lotto {
            val numbers = (LottoConstant.LOTTO_RANGE)
                .shuffled().take(LottoConstant.LOTTO_COUNT)
            return Lotto(numbers)
        }
    }
}
