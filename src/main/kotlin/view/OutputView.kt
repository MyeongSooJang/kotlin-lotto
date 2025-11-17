package view

class OutputView {
    fun showLottoTotalPrice(lottoTotalPrice: Int){
        println("로또 총 금액은 ${lottoTotalPrice}원 입니다")
    }

    fun showChange(change: Int){
        println("거스롬돈은 ${change} 원입니다")
    }
}