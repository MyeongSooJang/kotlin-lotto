package domain

enum class PurchaseType(val value: Int) {
    AUTO(1),
    MANUAL(2);

    companion object {
        fun from(value: Int): PurchaseType {
            return entries.find { it.value == value } ?: throw NoSuchElementException("1과 2중에서 입력하세요")
        }
    }
}