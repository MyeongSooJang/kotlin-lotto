package view

open class InputView {
    companion object {
        val INPUT_MESSAGE = """
            === 코틀린 로또 머신 ===
            구입할 로또의 형태를 선택해주세요 (1과 2중에서 선택 부탁드립니다)
            1. 자동 2. 수동
        """.trimIndent()

        const val PURCHASE_AMOUNT_MESSAGE = "로또를 몇 장 구매하시겠습니까?(1장당 1000원)"
        const val INPUT_MONEY_MESSAGE = "결제할 금액을 입력해주세요."
        const val MANUAL_LOTTO_MESSAGE = "%d번째 구매 하실 로또 번호 6개를, (쉼표),로 구분하여 입력해주세요."
        const val WINNING_LOTTO_MESSAGE = "당첨 번호 6개를 입력해주세요."
        const val BONUS_NUMBER_MESSAGE = "보너스 번호 입력해주세요"

    }

    open fun readPurchaseType() : String {
        println(INPUT_MESSAGE)
        return readln()
    }

    open fun readPurchaseCount() : String{
        println(PURCHASE_AMOUNT_MESSAGE)
        return readln()
    }

    open fun readInputMoney() : String {
        println(INPUT_MONEY_MESSAGE)
        return readln()
    }

    open fun readManualLottoNumbers(index : Int) : String {
        println(MANUAL_LOTTO_MESSAGE.format(index))
        return readln()
    }

    open fun readWinningLottoNumbers() : String {
        println(WINNING_LOTTO_MESSAGE)
        return readln()
    }

    open fun readBonusNumber() : String {
        println(BONUS_NUMBER_MESSAGE)
        return readln()
    }
}