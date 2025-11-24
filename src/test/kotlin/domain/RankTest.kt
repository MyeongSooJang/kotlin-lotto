package domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RankTest : DescribeSpec({
    describe("Rank Enum") {

        context("각 등수의 속성 검증") {

            it("1등: 6개 일치, 보너스 불일치, 상금 2,000,000,000원") {
                Rank.FIRST.matchCount shouldBe 6
                Rank.FIRST.matchBonusNumber shouldBe false
                Rank.FIRST.prize shouldBe 2_000_000_000
            }

            it("2등: 5개 일치, 보너스 일치, 상금 30,000,000원") {
                Rank.SECOND.matchCount shouldBe 5
                Rank.SECOND.matchBonusNumber shouldBe true
                Rank.SECOND.prize shouldBe 30_000_000
            }

            it("3등: 5개 일치, 보너스 불일치, 상금 1,500,000원") {
                Rank.THIRD.matchCount shouldBe 5
                Rank.THIRD.matchBonusNumber shouldBe false
                Rank.THIRD.prize shouldBe 1_500_000
            }

            it("4등: 4개 일치, 보너스 불일치, 상금 50,000원") {
                Rank.FOURTH.matchCount shouldBe 4
                Rank.FOURTH.matchBonusNumber shouldBe false
                Rank.FOURTH.prize shouldBe 50_000
            }

            it("5등: 3개 일치, 보너스 불일치, 상금 5,000원") {
                Rank.FIFTH.matchCount shouldBe 3
                Rank.FIFTH.matchBonusNumber shouldBe false
                Rank.FIFTH.prize shouldBe 5_000
            }

            it("낙첨: 0개 일치, 보너스 불일치, 상금 0원") {
                Rank.NONE.matchCount shouldBe 0
                Rank.NONE.matchBonusNumber shouldBe false
                Rank.NONE.prize shouldBe 0
            }
        }

        context("getMatchInfo 메소드") {

            it("1등은 '6개 일치' 정보를 반환한다") {
                Rank.FIRST.getMatchInfo() shouldBe "6개 일치"
            }

            it("2등은 '5개 일치, 보너스 번호 일치' 정보를 반환한다") {
                Rank.SECOND.getMatchInfo() shouldBe "5개 일치, 보너스 번호 일치"
            }

            it("3등은 '5개 일치' 정보를 반환한다") {
                Rank.THIRD.getMatchInfo() shouldBe "5개 일치"
            }

            it("4등은 '4개 일치' 정보를 반환한다") {
                Rank.FOURTH.getMatchInfo() shouldBe "4개 일치"
            }

            it("5등은 '3개 일치' 정보를 반환한다") {
                Rank.FIFTH.getMatchInfo() shouldBe "3개 일치"
            }

            it("낙첨은 '0개 일치' 정보를 반환한다") {
                Rank.NONE.getMatchInfo() shouldBe "0개 일치"
            }
        }
    }

    describe("findRank 메소드") {

        context("1등 (6개 일치)") {

            it("6개 일치하고 보너스가 불일치하면 1등을 반환한다") {
                val rank = Rank.findRank(6, false)

                rank shouldBe Rank.FIRST
            }
        }

        context("2등 (5개 일치 + 보너스)") {

            it("5개 일치하고 보너스가 일치하면 2등을 반환한다") {
                val rank = Rank.findRank(5, true)

                rank shouldBe Rank.SECOND
            }
        }

        context("3등 (5개 일치)") {

            it("5개 일치하고 보너스가 불일치하면 3등을 반환한다") {
                val rank = Rank.findRank(5, false)

                rank shouldBe Rank.THIRD
            }
        }

        context("4등 (4개 일치)") {

            it("4개 일치하면 4등을 반환한다") {
                val rank = Rank.findRank(4, false)

                rank shouldBe Rank.FOURTH
            }
        }

        context("5등 (3개 일치)") {

            it("3개 일치하면 5등을 반환한다") {
                val rank = Rank.findRank(3, false)

                rank shouldBe Rank.FIFTH
            }
        }

        context("낙첨 (2개 이하 일치)") {

            it("2개 일치하고 보너스가 불일치하면 낙첨이다") {
                val rank = Rank.findRank(2, false)

                rank shouldBe Rank.NONE
            }

            it("2개 일치하고 보너스가 일치해도 낙첨이다") {
                val rank = Rank.findRank(2, true)

                rank shouldBe Rank.NONE
            }

            it("1개 일치하고 보너스가 불일치하면 낙첨이다") {
                val rank = Rank.findRank(1, false)

                rank shouldBe Rank.NONE
            }

            it("1개 일치하고 보너스가 일치해도 낙첨이다") {
                val rank = Rank.findRank(1, true)

                rank shouldBe Rank.NONE
            }

            it("0개 일치하면 낙첨이다") {
                val rank = Rank.findRank(0, false)

                rank shouldBe Rank.NONE
            }

            it("0개 일치하고 보너스가 일치해도 낙첨이다") {
                val rank = Rank.findRank(0, true)

                rank shouldBe Rank.NONE
            }
        }

        context("비정상 입력") {

            it("음수 개수가 입력되면 낙첨을 반환한다") {
                val rank = Rank.findRank(-1, false)

                rank shouldBe Rank.NONE
            }

            it("7개 이상 일치는 낙첨을 반환한다 (실제로는 불가능한 케이스)") {
                val rank = Rank.findRank(7, false)

                rank shouldBe Rank.NONE
            }

            it("매우 큰 숫자가 입력되어도 낙첨을 반환한다") {
                val rank = Rank.findRank(100, false)

                rank shouldBe Rank.NONE
            }
        }
    }

    describe("상금 및 등수 검증") {

        context("등수별 상금 검증") {

            it("1등의 상금은 2,000,000,000원이다") {
                Rank.FIRST.prize shouldBe 2_000_000_000
            }

            it("2등의 상금은 30,000,000원이다") {
                Rank.SECOND.prize shouldBe 30_000_000
            }

            it("3등의 상금은 1,500,000원이다") {
                Rank.THIRD.prize shouldBe 1_500_000
            }

            it("4등의 상금은 50,000원이다") {
                Rank.FOURTH.prize shouldBe 50_000
            }

            it("5등의 상금은 5,000원이다") {
                Rank.FIFTH.prize shouldBe 5_000
            }

            it("낙첨의 상금은 0원이다") {
                Rank.NONE.prize shouldBe 0
            }
        }
    }
})