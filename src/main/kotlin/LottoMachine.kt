import domain.Count
import domain.Lotto
import domain.LottoBundle
import domain.LottoConstant
import domain.LottoGenerator
import domain.LottoResult
import domain.Money
import domain.PurchaseType
import domain.Result.Failure
import domain.Result.Success
import domain.WinningNumbers
import view.InputView
import view.OutputView

class LottoMachine(
    val inputView: InputView,
    val outputView: OutputView
) {

    fun run() {
        val purchaseType = getPurchaseType()
        val purchaseCount = getPurchaseCount()

        val totalPrice = purchaseCount * LottoConstant.LOTTO_PRICE
        outputView.showLottoTotalPrice(totalPrice)

        val payment = getPayment()

        require(payment >= totalPrice) {
            "금액이 부족합니다. 필요: ${totalPrice.amount}원, 제공: ${payment.amount}원"
        }

        val change = payment - totalPrice

        if (change.amount > 0) {
            outputView.showChange(change)
        }

        val lottoBundle = generateLottos(purchaseType, purchaseCount)
        outputView.showPurchasedLottos(lottoBundle)

        val winningNumbers = getWinningNumbers()

        val rankResults = lottoBundle.checkRanks(winningNumbers)
        val lottoResult = LottoResult(rankResults)

        outputView.showFinalResult(lottoResult, totalPrice.amount)
    }

    private fun getPurchaseType(): PurchaseType {
        val result = inputView.readPurchaseType().toPurchaseType()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPurchaseType()
            }
        }
    }

    private fun getPurchaseCount(): Count {
        val result = inputView.readPurchaseCount().toPurchaseCount()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPurchaseCount()
            }
        }
    }

    private fun getPayment(): Money {
        val result = inputView.readInputMoney().toMoney()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPayment()
            }
        }
    }

    private fun generateLottos(
        purchaseType: PurchaseType,
        purchaseCount: Count
    ): LottoBundle = with(purchaseCount.value) {
        LottoBundle(
            when (purchaseType) {
                PurchaseType.AUTO -> generateAutoLottos(this)
                PurchaseType.MANUAL -> generateManualLottos(this)
            }
        )
    }

    private fun generateAutoLottos(count: Int): List<Lotto> =
        List(count) { LottoGenerator.generate() }

    private fun generateManualLottos(count: Int): List<Lotto> =
        List(count) { it ->
            getLottoNumbers(it)
                .let(LottoGenerator::generate)
        }

    private fun getLottoNumbers(index: Int): List<Int> {
        val result = inputView.readManualLottoNumbers(index).toLottoNumbers()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getLottoNumbers(index)
            }
        }
    }

    private fun getWinningNumbers(): WinningNumbers = WinningNumbers(
        numbers = getWinningLottoNumbers(),
        bonusNumber = getBonusNumber()
    )

    private fun getWinningLottoNumbers(): List<Int> {
        val result = inputView.readWinningLottoNumbers().toLottoNumbers()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getWinningLottoNumbers()
            }
        }
    }

    private fun getBonusNumber(): Int {
        val result = inputView.readBonusNumber().toBonusNumber()
        return when (result) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getBonusNumber()
            }
        }
    }

}
