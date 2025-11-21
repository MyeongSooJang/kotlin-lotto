package domain

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import view.InputView

class LottoMachineTest : DescribeSpec({
    describe("LottoMachine") {

        context("generateLottos 메소드") {

            it("자동 구매로 1장의 로또를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "1"
                    override fun readInputMoney(): String = "1000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.AUTO
                val purchaseCount = Count(1)
                val result = lottoMachine.generateLottos(purchaseType, purchaseCount)

                result.getLotto() shouldHaveSize 1
            }

            it("자동 구매로 5장의 로또를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "5"
                    override fun readInputMoney(): String = "5000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.AUTO
                val purchaseCount = Count(5)
                val result = lottoMachine.generateLottos(purchaseType, purchaseCount)

                result.getLotto() shouldHaveSize 5
            }

            it("수동 구매로 3장의 로또를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "2"
                    override fun readPurchaseCount(): String = "3"
                    override fun readInputMoney(): String = "3000"
                    override fun readManualLottoNumbers(index: Int): String = when (index) {
                        0 -> "1,2,3,4,5,6"
                        1 -> "7,8,9,10,11,12"
                        else -> "13,14,15,16,17,18"
                    }
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.MANUAL
                val purchaseCount = Count(3)
                val result = lottoMachine.generateLottos(purchaseType, purchaseCount)

                result.getLotto() shouldHaveSize 3
            }

            it("생성된 모든 로또는 유효한 형태이다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "3"
                    override fun readInputMoney(): String = "3000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.AUTO
                val purchaseCount = Count(3)
                val result = lottoMachine.generateLottos(purchaseType, purchaseCount)

                result.getLotto().forEach { lotto ->
                    lotto.lottoNumbers shouldHaveSize 6
                    lotto.lottoNumbers.all { it in 1..45 } shouldBe true
                    lotto.lottoNumbers shouldBe lotto.lottoNumbers.sorted()
                }
            }
        }

        context("playLottery 메소드") {

            it("로또 묶음과 당첨 번호로 결과를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "2"
                    override fun readInputMoney(): String = "2000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val lotto1 = Lotto(listOf(1, 2, 3, 4, 5, 6))
                val lotto2 = Lotto(listOf(7, 8, 9, 10, 11, 12))
                val lottoBundle = LottoBundle(listOf(lotto1, lotto2))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val result = lottoMachine.playLottery(lottoBundle, winningNumbers)

                result.bundleResult[Rank.FIRST] shouldBe 1
                result.bundleResult[Rank.NONE] shouldBe 1
            }

            it("1등과 2등이 섞여 있는 결과를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "2"
                    override fun readInputMoney(): String = "2000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val lotto1 = Lotto(listOf(1, 2, 3, 4, 5, 6))
                val lotto2 = Lotto(listOf(1, 2, 3, 4, 5, 7))
                val lottoBundle = LottoBundle(listOf(lotto1, lotto2))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val result = lottoMachine.playLottery(lottoBundle, winningNumbers)

                result.bundleResult[Rank.FIRST] shouldBe 1
                result.bundleResult[Rank.SECOND] shouldBe 1
            }

            it("모두 낙첨된 결과를 생성한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "3"
                    override fun readInputMoney(): String = "3000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val lotto1 = Lotto(listOf(10, 11, 12, 13, 14, 15))
                val lotto2 = Lotto(listOf(20, 21, 22, 23, 24, 25))
                val lotto3 = Lotto(listOf(30, 31, 32, 33, 34, 35))
                val lottoBundle = LottoBundle(listOf(lotto1, lotto2, lotto3))

                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val result = lottoMachine.playLottery(lottoBundle, winningNumbers)

                result.bundleResult[Rank.NONE] shouldBe 3
            }

            it("빈 로또 묶음은 빈 결과를 반환한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "0"
                    override fun readInputMoney(): String = "0"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val lottoBundle = LottoBundle(emptyList())
                val winningNumbers = WinningNumbers(listOf(1, 2, 3, 4, 5, 6), 7)
                val result = lottoMachine.playLottery(lottoBundle, winningNumbers)

                result.bundleResult shouldBe emptyMap()
            }
        }

        context("calculateChange 메소드") {

            it("결제 금액에서 총액을 빼서 거스름돈을 계산한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "5"
                    override fun readInputMoney(): String = "10000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val payment = Money(10000)
                val totalPrice = Money(5000)
                val result = lottoMachine.calculateChange(payment, totalPrice)

                result.amount shouldBe 5000L
            }

            it("정확히 결제된 경우 거스름돈은 0원이다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "5"
                    override fun readInputMoney(): String = "5000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val payment = Money(5000)
                val totalPrice = Money(5000)
                val result = lottoMachine.calculateChange(payment, totalPrice)

                result.amount shouldBe 0L
            }

            it("큰 금액의 거스름돈도 정확히 계산한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "5"
                    override fun readInputMoney(): String = "1000000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val payment = Money(1000000)
                val totalPrice = Money(5000)
                val result = lottoMachine.calculateChange(payment, totalPrice)

                result.amount shouldBe 995000L
            }
        }

        context("getWinningNumbers 메소드") {

            it("당첨 번호와 보너스 번호를 정확히 파싱한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "1"
                    override fun readInputMoney(): String = "1000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val winningNumbers = lottoMachine.getWinningNumbers()

                winningNumbers.numbers shouldBe listOf(1, 2, 3, 4, 5, 6)
                winningNumbers.bonusNumber shouldBe 7
            }

            it("다양한 당첨 번호를 파싱한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "1"
                    override fun readInputMoney(): String = "1000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "10,20,30,40,41,42"
                    override fun readBonusNumber(): String = "45"
                }
                val lottoMachine = LottoMachine(inputView)

                val winningNumbers = lottoMachine.getWinningNumbers()

                winningNumbers.numbers shouldBe listOf(10, 20, 30, 40, 41, 42)
                winningNumbers.bonusNumber shouldBe 45
            }
        }

        context("통합 플로우") {

            it("자동 구매로 1장을 구매하고 당첨을 확인한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "1"
                    override fun readPurchaseCount(): String = "1"
                    override fun readInputMoney(): String = "2000"
                    override fun readManualLottoNumbers(index: Int): String = ""
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.AUTO
                val purchaseCount = Count(1)
                val totalPrice = purchaseCount * LottoConstant.LOTTO_PRICE
                val payment = Money(2000)

                val change = lottoMachine.calculateChange(payment, totalPrice)
                change.amount shouldBe 1000L

                val lottoBundle = lottoMachine.generateLottos(purchaseType, purchaseCount)
                lottoBundle.getLotto() shouldHaveSize 1

                val winningNumbers = lottoMachine.getWinningNumbers()
                val lottoResult = lottoMachine.playLottery(lottoBundle, winningNumbers)

                lottoResult.bundleResult shouldBe mapOf(Rank.FIRST to 1)
                lottoResult.totalPrize shouldBe 2_000_000_000L
            }

            it("수동으로 3장을 구매하고 결과를 확인한다") {
                val inputView = object : InputView() {
                    override fun readPurchaseType(): String = "2"
                    override fun readPurchaseCount(): String = "3"
                    override fun readInputMoney(): String = "5000"
                    override fun readManualLottoNumbers(index: Int): String = when (index) {
                        0 -> "1,2,3,4,5,6"
                        1 -> "1,2,3,4,5,7"
                        else -> "10,11,12,13,14,15"
                    }
                    override fun readWinningLottoNumbers(): String = "1,2,3,4,5,6"
                    override fun readBonusNumber(): String = "7"
                }
                val lottoMachine = LottoMachine(inputView)

                val purchaseType = PurchaseType.MANUAL
                val purchaseCount = Count(3)
                val totalPrice = purchaseCount * LottoConstant.LOTTO_PRICE
                val payment = Money(5000)

                val change = lottoMachine.calculateChange(payment, totalPrice)
                change.amount shouldBe 2000L

                val lottoBundle = lottoMachine.generateLottos(purchaseType, purchaseCount)
                lottoBundle.getLotto() shouldHaveSize 3

                val winningNumbers = lottoMachine.getWinningNumbers()
                val lottoResult = lottoMachine.playLottery(lottoBundle, winningNumbers)

                lottoResult.bundleResult[Rank.FIRST] shouldBe 1
                lottoResult.bundleResult[Rank.SECOND] shouldBe 1
                lottoResult.bundleResult[Rank.NONE] shouldBe 1
            }
        }
    }
})
