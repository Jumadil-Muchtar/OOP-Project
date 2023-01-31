class ReturnTransaction(orderTrx: OrderTransaction, code: String) {
    private var confirmationCode : String = code
    val orderTransaction: OrderTransaction = orderTrx
    val idOrderTransaction: Long = orderTrx.idOrderTransaction

    fun isValidCode(codeParams: String): Boolean{
        when(this.confirmationCode){
            codeParams-> return true
            else -> return false
        }
    }
    fun getConfirmationCode(): String{
        return this.confirmationCode
    }



}