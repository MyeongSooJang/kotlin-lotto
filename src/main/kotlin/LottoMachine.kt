import domain.Lotto
import domain.LottoBundle
import domain.LottoConstant
import domain.LottoConstant.PURCHASE_COUNT_RANGE
import domain.LottoGenerator
import domain.LottoResult
import domain.PurchaseType
import domain.WinningNumbers
import view.InputView
import view.OutputView

class LottoMachine(
    val inputView: InputView,
    val outputView: OutputView
) {

    fun run() {
        val purchaseType = getPurchaseType()
        val purchaseCount = getPurchaseAmount()

        val totalPrice = purchaseCount * LottoConstant.LOTTO_PRICE
        val payment = getPayment(totalPrice)
        val change = payment - totalPrice

        if (change > 0) {
            outputView.showChange(change)
        }

        val lottoBundle = generateLottos(purchaseType, purchaseCount)
        outputView.showPurchasedLottos(lottoBundle)

        val winningNumbers = getWinningNumbers()

        val rankResults = lottoBundle.checkRanks(winningNumbers)
        val lottoResult = LottoResult(rankResults)

        outputView.showFinalResult(lottoResult, totalPrice.toLong())
    }

    private fun getPurchaseType(): PurchaseType {
        val input = inputView.readPurchaseType().toInt()
        return PurchaseType.from(input)
    }

    private fun getPurchaseAmount(): Int {
        val purchaseAmount = inputView.readPurchaseCount()
        val count = purchaseAmount.toIntOrNull() ?: throw IllegalArgumentException("유효한 숫자를 입력해주세요.")

        require(count in PURCHASE_COUNT_RANGE) {
            "로또는 1~100장까지 구매 가능합니다."
        }
        return count
    }


    private fun getPayment(totalMoney: Int): Int {
        outputView.showLottoTotalPrice(totalMoney)

        val input = inputView.readInputMoney()
        val payment = input.toIntOrNull()
            ?: throw IllegalArgumentException("유효한 금액을 입력해주세요.")

        require(payment >= totalMoney) {
            "최소 ${totalMoney}원이 필요합니다. (${totalMoney - payment}원 부족)"
        }

        return payment

    }

    private fun generateLottos(purchaseType: PurchaseType,
                               purchaseCount: Int): LottoBundle {

        val lottos = when (purchaseType) {
            PurchaseType.AUTO -> generateAutoLottos(purchaseCount)
            PurchaseType.MANUAL -> generateManualLottos(purchaseCount)

        }
        return LottoBundle(lottos)
    }

    private fun generateAutoLottos(count: Int): List<Lotto> {
        return List(count){ LottoGenerator.generate()}
    }

    private fun generateManualLottos(count: Int): List<Lotto> {
        return List(count){ it ->
            val numbersInput = inputView.readManualLottoNumbers(it)
            val numbers = parseNumbers(numbersInput)
            LottoGenerator.generate(numbers)
        }
    }

    private fun parseNumbers(input: String): List<Int> {
        return input.split(",")
            .map { it.trim().toIntOrNull()
                ?: throw IllegalArgumentException("유효한 숫자를 입력해주세요.")
            }
    }

    private fun getWinningNumbers(): WinningNumbers {
        val numbersInput = inputView.readWinningLottoNumbers()
        val numbers = parseNumbers(numbersInput)

        val bonusInput = inputView.readBonusNumber()
        val bonus = bonusInput.toIntOrNull()
            ?: throw IllegalArgumentException("유효한 숫자를 입력해주세요.")

        return WinningNumbers(numbers, bonus)
    }


}