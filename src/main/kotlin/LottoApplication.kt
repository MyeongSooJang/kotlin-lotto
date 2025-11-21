package controller

import LottoController
import view.InputView
import view.OutputView

fun main() {
    LottoController(
        inputView = InputView(),
        outputView = OutputView()
    ).apply { run() }
}
