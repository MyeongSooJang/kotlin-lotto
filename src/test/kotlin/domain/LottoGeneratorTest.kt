package domain

import domain.LottoGenerator.Companion.LOTTO_NUMBER_COUNT
import domain.LottoGenerator.Companion.LOTTO_NUMBER_RANGE
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class LottoGeneratorTest : DescribeSpec({
    describe("LottoGenerator") {

        context("generate 함수가 실행이 되면") {

            it("무작위 숫자 6개가 생성이 된다") {

                val result = LottoGenerator.generate()
                result.size shouldBe LOTTO_NUMBER_COUNT
            }

            it("중복된 숫자는 존재하지 않는다") {
                val result = LottoGenerator.generate()
                result.toSet().size shouldBe result.size
            }

            it("숫자들의 범위는 모두 1부터 45이다") {
                val result = LottoGenerator.generate()
                result.forAll { it in LOTTO_NUMBER_RANGE }

            }
        }
    }
})