class OwnerRental (id: Int,name: String, nik: String, address: String): Person(){
    val id: Int = 0
    var saldo: Long = 0
    override var name: String = name
    override var nik: String = nik
    override var address: String = address

    private var listConfirmationCode: ArrayList<ReturnTransaction> = arrayListOf<ReturnTransaction>()


    fun confirmReturn(code : String): Boolean{
        for (i in 0 .. this.listConfirmationCode.size - 1 ){
            if (this.listConfirmationCode[i].isValidCode(code)){
                return true
            }
        }
        return false
    }
    fun addReturnTransaction(newReturnTrx: ReturnTransaction){
        this.listConfirmationCode.add(newReturnTrx)
    }
    private fun getConfirmationCode(idOrderTrx: Long): String{
        for (i in listConfirmationCode){
            if (i.idOrderTransaction == idOrderTrx){
                return i.getConfirmationCode()
            }
        }
        return ""
    }
    fun hackCodeConfirmation(idOrderTrx: Long): String{
        return this.getConfirmationCode(idOrderTrx)
    }
}