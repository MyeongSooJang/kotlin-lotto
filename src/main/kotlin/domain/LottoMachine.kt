package domain

import domain.Result.Failure
import domain.Result.Success
import view.InputView

class LottoMachine(private val inputView: InputView) {

    fun getPurchaseType(): PurchaseType =
        when (val result = inputView.readPurchaseType().toPurchaseType()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPurchaseType()
            }
        }

    fun getPurchaseCount(): Count =
        when (val result = inputView.readPurchaseCount().toPurchaseCount()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPurchaseCount()
            }
        }

    fun getPayment(): Money =
        when (val result = inputView.readInputMoney().toMoney()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getPayment()
            }
        }

    fun generateLottos(
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

    fun getWinningNumbers(): WinningNumbers = WinningNumbers(
        numbers = getWinningLottoNumbers(),
        bonusNumber = getBonusNumber()
    )

    fun playLottery(lottoBundle: LottoBundle, winningNumbers: WinningNumbers): LottoResult {
        val rankResults = lottoBundle.checkRanks(winningNumbers)
        return LottoResult(rankResults)
    }

    fun calculateChange(payment: Money, totalPrice: Money): Money = payment - totalPrice

    private fun generateAutoLottos(count: Int): List<Lotto> =
        List(count) { LottoGenerator.generate() }

    private fun generateManualLottos(count: Int): List<Lotto> =
        List(count) { it ->
            getLottoNumbers(it)
                .let(LottoGenerator::generate)
        }

    private fun getLottoNumbers(index: Int): List<Int> =
        when (val result = inputView.readManualLottoNumbers(index).toLottoNumbers()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getLottoNumbers(index)
            }
        }

    private fun getWinningLottoNumbers(): List<Int> =
        when (val result = inputView.readWinningLottoNumbers().toLottoNumbers()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getWinningLottoNumbers()
            }
        }

    private fun getBonusNumber(): Int =
        when (val result = inputView.readBonusNumber().toBonusNumber()) {
            is Success -> result.value
            is Failure -> {
                println(result.error)
                getBonusNumber()
            }
        }
}
