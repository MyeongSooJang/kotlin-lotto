package dto

import domain.LottoBundle
import domain.Money

data class PurchaseData(
    val totalPrice: Money,
    val change: Money,
    val lottoBundle: LottoBundle
)
