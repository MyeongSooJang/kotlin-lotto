import domain.Count
import domain.Lotto
import domain.LottoBundle
import domain.LottoConstant
import domain.LottoGenerator
import domain.LottoResult
import domain.Money
import domain.PurchaseType
import domain.WinningNumbers
import view.InputView
import view.OutputView

class LottoMachine(
    val inputView: InputView,
    val outputView: OutputView
) {

    fun run() {
        val purchaseType = inputView.readPurchaseType().toPurchaseType()
        val purchaseCount = inputView.readPurchaseCount().toPurchaseCount()

        val totalPrice = purchaseCount * LottoConstant.LOTTO_PRICE
        outputView.showLottoTotalPrice(totalPrice)

        val payment = inputView.readInputMoney().toMoney()

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

    private fun generateLottos(
        purchaseType: PurchaseType,
        purchaseCount: Count
    ): LottoBundle = LottoBundle(
        when (purchaseType) {
            PurchaseType.AUTO -> generateAutoLottos(purchaseCount.value)
            PurchaseType.MANUAL -> generateManualLottos(purchaseCount.value)
        }
    )

    private fun generateAutoLottos(count: Int): List<Lotto> =
        List(count) { LottoGenerator.generate() }

    private fun generateManualLottos(count: Int): List<Lotto> =
        List(count) { it ->
            inputView.readManualLottoNumbers(it)
                .toLottoNumbers()
                .let { LottoGenerator.generate(it) }
        }

    private fun getWinningNumbers(): WinningNumbers = WinningNumbers(
        numbers = inputView.readWinningLottoNumbers().toLottoNumbers(),
        bonusNumber = inputView.readBonusNumber().toBonusNumber()
    )

}
