package controller

import service.LottoService
import view.InputView
import view.OutputView

class LottoController(
    inputView: InputView,
    private val outputView: OutputView
) {
    private val lottoService = LottoService(inputView)

    fun run() {
        val gameData = lottoService.play()

        outputView.showLottoTotalPrice(gameData.totalPrice)

        if (gameData.change.amount > 0) {
            outputView.showChange(gameData.change)
        }

        outputView.showPurchasedLottos(gameData.lottoBundle)
        outputView.showFinalResult(gameData.lottoResult, gameData.totalPrice.amount)
    }
}
