import domain.LottoConstant
import domain.LottoConstant.PURCHASE_COUNT_RANGE
import domain.PurchaseType

fun String.toPurchaseType(): PurchaseType =
    toIntOrNull()?.let {PurchaseType.from(it)}
        ?: error("숫자를 입력해주세요")


fun String.toPurchaseCount(): Int {
    val count = toIntOrNull() ?: error("유효한 숫자를 입력해주세요")

    require(count in PURCHASE_COUNT_RANGE) {
        "로또는 1~100장까지 구매 가능합니다."
    }

    return count
}

fun String.toPayment(totalPrice: Int): Int {
    val payment = toIntOrNull() ?: error("유효한 숫자를 입력해주세요")

    require(payment >= totalPrice) {
        "최소 ${totalPrice}원이 필요합니다. (${totalPrice - payment}원 부족)"
    }

    return payment
}

fun String.toLottoNumbers(): List<Int> =
    split(",")
        .map {
            it.trim().toIntOrNull() ?: error("유효한 숫자를 입력해주세요")
        }

fun String.toBonusNumber(): Int {
    val bonus = toIntOrNull()
        ?: error("유효한 숫자를 입력해주세요")

    require(bonus in LottoConstant.LOTTO_RANGE) {
        "로또 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 ${LottoConstant.LOTTO_RANGE.last}까지여야 합니다"
    }

    return bonus
}