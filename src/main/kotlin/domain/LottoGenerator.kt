package domain

class LottoGenerator {

    companion object {
        fun generate(): Lotto =
            Lotto(
                LottoConstant.LOTTO_RANGE
                    .shuffled()
                    .take(LottoConstant.LOTTO_COUNT)
                    .sorted()
            )

        fun generate(numbers: List<Int>): Lotto =
            Lotto(numbers.sorted())
    }
}
