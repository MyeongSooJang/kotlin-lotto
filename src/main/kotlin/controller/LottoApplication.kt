package controller

import LottoMachine
import view.InputView
import view.OutputView

fun main() {
    val inputView = InputView()
    val outputView = OutputView()
    val lottoMachine = LottoMachine(inputView, outputView)

    lottoMachine.run()
}
