package dto

import domain.LottoBundle
import domain.LottoResult
import domain.Money

data class GameData(
    val totalPrice: Money,
    val change: Money,
    val lottoBundle: LottoBundle,
    val lottoResult: LottoResult
)
