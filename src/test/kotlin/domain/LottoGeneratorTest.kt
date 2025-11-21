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

            it("생성된 로또는 6개의 숫자 범위에 있다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers.size shouldBe LottoConstant.LOTTO_COUNT
                lotto.lottoNumbers.forAll { it shouldBeInRange LottoConstant.LOTTO_RANGE }
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

            it("정렬되지 않은 숫자들도 정렬되어 생성된다") {
                val numbers = listOf(45, 23, 1, 39, 12, 7)
                val lotto = LottoGenerator.generate(numbers)

                lotto.lottoNumbers shouldBe listOf(1, 7, 12, 23, 39, 45)
            }

            it("최소값과 최대값으로 이루어진 숫자들을 생성한다") {
                val numbers = listOf(1, 2, 3, 4, 5, 45)
                val lotto = LottoGenerator.generate(numbers)

                lotto.lottoNumbers shouldBe listOf(1, 2, 3, 4, 5, 45)
            }

            it("경계값(최소/최대)으로 생성해도 유효하다") {
                val numbers = listOf(1, 5, 10, 20, 35, 40)
                val lotto = LottoGenerator.generate(numbers)

                lotto.lottoNumbers.size shouldBe 6
                lotto.lottoNumbers shouldBe lotto.lottoNumbers.sorted()
            }

            it("같은 입력으로 생성하면 같은 결과가 나온다") {
                val numbers = listOf(10, 20, 30, 40, 41, 42)
                val lotto1 = LottoGenerator.generate(numbers)
                val lotto2 = LottoGenerator.generate(numbers)

                lotto1.lottoNumbers shouldBe lotto2.lottoNumbers
            }
        }

        context("생성된 로또의 특성") {

            it("생성된 로또는 정렬되어 있다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers shouldBe lotto.lottoNumbers.sorted()
            }

            it("생성된 로또는 중복이 없다") {
                val lotto = LottoGenerator.generate()
                lotto.lottoNumbers.distinct().size shouldBe 6
            }
        }
    }
})