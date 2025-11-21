package service

import domain.Count
import domain.LottoConstant
import domain.LottoMachine
import domain.Money
import dto.GameData
import dto.GameResult
import dto.PurchaseData
import view.InputView

class LottoService(inputView: InputView) {
    private val lottoMachine = LottoMachine(inputView)

    fun play(): GameData = doPurchase().let { purchaseData ->
        playGame(purchaseData).let { gameResult ->
            GameData(purchaseData.totalPrice, purchaseData.change,
                    gameResult.lottoBundle, gameResult.lottoResult)
        }
    }

    private fun doPurchase(): PurchaseData {
        val purchaseType = lottoMachine.getPurchaseType()
        val purchaseCount = lottoMachine.getPurchaseCount()
        val totalPrice = calculateTotalPrice(purchaseCount)
        val payment = lottoMachine.getPayment()

        return run {
            require(payment >= totalPrice) {
                "금액이 부족합니다. 필요: ${totalPrice.amount}원, 제공: ${payment.amount}원"
            }
            PurchaseData(totalPrice, calculateChange(payment, totalPrice),
                        lottoMachine.generateLottos(purchaseType, purchaseCount))
        }
    }

    private fun playGame(purchaseData: PurchaseData): GameResult =
        lottoMachine.getWinningNumbers().let { winningNumbers ->
            GameResult(purchaseData.lottoBundle,
                      lottoMachine.playLottery(purchaseData.lottoBundle, winningNumbers))
        }

    private fun calculateTotalPrice(purchaseCount: Count): Money =
        purchaseCount * LottoConstant.LOTTO_PRICE

    private fun calculateChange(payment: Money, totalPrice: Money): Money =
        lottoMachine.calculateChange(payment, totalPrice)
}
