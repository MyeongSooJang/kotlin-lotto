package dto

import domain.LottoBundle
import domain.LottoResult

data class GameResult(
    val lottoBundle: LottoBundle,
    val lottoResult: LottoResult
)
