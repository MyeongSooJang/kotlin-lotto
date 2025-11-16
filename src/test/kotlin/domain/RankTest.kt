package domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RankTest : DescribeSpec ({
    describe ("RankTest") {

        context("findRank 메소드"){

            it("당첨번호와 6개가 일치하면 1등을 반환"){
                val result = Rank.findRank(6,false) shouldBe Rank.FIRST
            }

            it("당첨번호와 5개 일치하고, 보너스번호가 일치하면 2등을 반환"){
                val result = Rank.findRank(5,true) shouldBe Rank.SECOND
            }

            it("당첨번호와 5개 일치하고, 보너스번호 일치하지 않으면 3등을 반환"){
                val result = Rank.findRank(5,false) shouldBe Rank.THIRD
            }

            it("당첨번호와 4개 일치하면 4등을 반환"){
                val result = Rank.findRank(4,false) shouldBe Rank.FORTH
            }

            it("당첨번호와 3개 일치히마녀 5등을 반환"){
                val result = Rank.findRank(3,false) shouldBe Rank.FIFTH
            }

            it("그 이하는 NONE을 반환"){
                val result1 = Rank.findRank(2,false) shouldBe Rank.NONE
                val result2 = Rank.findRank(1,false) shouldBe Rank.NONE
                val result3 = Rank.findRank(0,false) shouldBe Rank.NONE

            }

        }
    }
})