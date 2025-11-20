package domain

@JvmInline
value class Money(val amount: Long) {
    init {
        require(amount > 0) {
            "금액은 0보다 커야 합니다"
        }
    }

    operator fun times(count: Count) = Money(amount * count.value)
    operator fun minus(other: Money) = Money(amount - other.amount)
    operator fun plus(other: Money) = Money(amount + other.amount)
    operator fun compareTo(other: Money) = amount.compareTo(other.amount)
}

@JvmInline
value class Count(val value: Int) {
    init {
        require(value in 1..100) { "1~100장까지 구매 가능합니다" }
    }

    operator fun times(price: Money) = Money(price.amount * value)

}

