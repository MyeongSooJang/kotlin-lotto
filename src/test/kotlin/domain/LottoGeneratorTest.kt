package domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe

class LottoGeneratorTest : DescribeSpec({
    describe("LottoGenerator") {

        context("랜덤 generate()") {
            it("생성된 로또는 6개의 숫자를 가진다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers.size shouldBe 6
            }

            it("생성된 로또의 모든 숫자는 1~45 범위이다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers.forAll { number ->
                   number shouldBeInRange LottoConstant.LOTTO_RANGE
                }
            }

            it("생성된 로또는 중복된 숫자가 없다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers.size shouldBe lotto.lottoNumbers.distinct().size
            }

            it("여러 번 생성해도 모두 유효한 로또이다") {
                repeat(100) {
                    val lotto = LottoGenerator.generate()
                    lotto.lottoNumbers.size shouldBe LottoConstant.LOTTO_COUNT
                    lotto.lottoNumbers.forAll { it shouldBeInRange LottoConstant.LOTTO_RANGE }
                }
            }

            it("생성된 로또는 정렬되어 있다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers shouldBe lotto.lottoNumbers.sorted()
            }
        }

        context("수동 generate(numbers)") {
            it("정상적인 숫자 6개를 넘겨주면 Lotto를 생성한다") {
                val numbers = listOf(1, 2, 3, 4, 5, 6)
                val lotto = LottoGenerator.generate(numbers)

                lotto.lottoNumbers shouldBe listOf(1, 2, 3, 4, 5, 6)
            }

        }
    }
})