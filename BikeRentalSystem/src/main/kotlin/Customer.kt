class Customer (id: Int, name: String, nik: String, address: String): Person(){
    val id: Int = 0
    var saldo: Long = 0
        private set
    override var name: String = name
    override var nik: String = nik
    override var address: String = address


//    var listOfMyBikeRented : ArrayList<Bikes> = arrayListOf<Bikes>()
    var listOfMyOrderTransaction : ArrayList<OrderTransaction> = arrayListOf<OrderTransaction>()

    public fun topupSaldo(){
        println("How many money to topup your Account?")
        print("Rp. ")
        val topup: Long = readln().toLong()
        this.saldo += topup
        println("Hi ${this.name}! Kamu sedang melakukan topup sebesar $topup. Sisa saldonya = ${this.saldo}")
    }
    fun updateTransactionHistory(newOrder: OrderTransaction){
        this.listOfMyOrderTransaction.add(newOrder)
    }
    fun payRental(cost: Long): String{
        println("Really to pay this Rental?")
        println("1. Yes")
        println("0. Back")
        println("Please input the number of your option")
        var isPay = readln().toInt()

        if (isPay == 1){

            var sisaSaldo= this.saldo - cost
            if (sisaSaldo >=0){
                this.saldo-=cost
            }
            return sisaSaldo.toString()
        }else if (isPay == 0){
            return "Back"
        }else{
            return "Input is false"
        }
    }
    fun addMyTransactionOrder(newTrx: OrderTransaction): Boolean{
        for (i in listOfMyOrderTransaction){
            if(i.idOrderTransaction == newTrx.idOrderTransaction){
                return false
            }
        }
        this.listOfMyOrderTransaction.add(newTrx)
        return true
    }
    fun updateStateTransaction(isReturnBike: Boolean, idTrx: Long):Boolean{
        for (i in 0..listOfMyOrderTransaction.size-1){
            if(this.listOfMyOrderTransaction[i].idOrderTransaction == idTrx){
                this.listOfMyOrderTransaction[i].returnBike()
                return true
            }
        }
        return false
    }
}