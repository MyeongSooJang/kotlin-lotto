import domain.Count
import domain.LottoConstant
import domain.Money
import domain.Result.Success
import domain.Result.Failure
import domain.PurchaseType
import domain.Result

fun String.toPurchaseType(): Result<PurchaseType> =
    toIntOrNull()?.let { Success(PurchaseType.from(it)) }
        ?: Failure("숫자를 입력해주세요")

fun String.toPurchaseCount(): Result<Count> =
    toIntOrNull()?.let { Success(Count(it)) }
        ?: Failure("유효한 숫자를 입력해주세요")

fun String.toMoney(): Result<Money> =
    toLongOrNull()?.let { Success(Money(it)) }
        ?: Failure("유효한 금액을 입력해주세요")

fun String.toLottoNumbers(): Result<List<Int>> {
    val numbers = split(",").map { it.trim().toIntOrNull() }
    if (numbers.any { it == null }) return Failure("유효한 숫자를 입력해주세요")
    return Success(numbers.filterNotNull())
}

fun String.toBonusNumber(): Result<Int> {
    val bonus = toIntOrNull() ?: return Failure("유효한 숫자를 입력해주세요")
    if (bonus !in LottoConstant.LOTTO_RANGE) {
        return Failure(
            "로또 번호는 ${LottoConstant.LOTTO_RANGE.first}부터 " +
                    "${LottoConstant.LOTTO_RANGE.last}까지여야 합니다"
        )
    }
    return Success(bonus)
}

