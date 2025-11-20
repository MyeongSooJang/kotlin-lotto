package controller

import LottoMachine
import view.InputView
import view.OutputView

fun main() {
    LottoMachine(
        inputView = InputView(),
        outputView = OutputView()
    ).apply { run() }
}
