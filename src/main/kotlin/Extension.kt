import domain.Count
import domain.LottoConstant
import domain.Money
import domain.PurchaseType

fun String.toPurchaseType(): PurchaseType =
    toIntOrNull()?.let { PurchaseType.from(it) }
        ?: error("숫자를 입력해주세요")

fun String.toPurchaseCount(): Count {
    val count = toIntOrNull() ?: error("유효한 숫자를 입력해주세요")
    return Count(count)  // Count의 init에서 검증됨
}

fun String.toMoney(): Money {
    val amount = toLongOrNull() ?: error("유효한 금액을 입력해주세요")
    return Money(amount)
}

fun String.toLottoNumbers(): List<Int> =
    split(",")
        .map {
            it.trim().toIntOrNull() ?: error("유효한 숫자를 입력해주세요")
        }

fun String.toBonusNumber(): Int =
    toIntOrNull().apply {
        require(this in LottoConstant.LOTTO_RANGE) {
            "로또 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 ${LottoConstant.LOTTO_RANGE.last}까지여야 합니다"
        }
    } ?: error("유효한 숫자를 입력해주세요")

