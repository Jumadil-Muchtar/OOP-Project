import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.time.Duration

class Session(customer: ArrayList<Customer>,
              ownerRental: ArrayList<OwnerRental>,
              bikes: ArrayList<Bikes>,
              order: ArrayList<OrderTransaction>
)
{
    constructor(): this(arrayListOf<Customer>(), arrayListOf<OwnerRental>(), arrayListOf<Bikes>(), arrayListOf<OrderTransaction>())

    val listCustomer: ArrayList<Customer> = customer
    val listOwnerRental: ArrayList<OwnerRental> = ownerRental
    val listBikes: ArrayList<Bikes> = bikes
    val listOrderTransaction: ArrayList<OrderTransaction> = order

    fun addCustomer(name: String, nik: String, address: String){
        if(this.listCustomer.size > 0){
            var lastId = this.getLastIdCustomer()
            var newCustomer = Customer(lastId, name, nik, address)
        }else{
            var newCustomer = Customer(0, name, nik, address)
        }
    }
    fun addCustomerObj(customerr : Customer){

        this.listCustomer.add(customerr)
    }
    fun isAnyCustomer(nameParams: String): Boolean{
        for(i in this.listCustomer){
            if (i.name.equals(nameParams)){
                return true
            }
        }
        return false
    }
    fun getCustomer(name: String): Customer{
        for(i in this.listCustomer){
            if (i.name.equals(name)){
                return i
            }
        }
        return Customer(0,"", "", "")
    }
    fun getIndexCustomer(name: String): Int{
        for(i in 0 .. this.listCustomer.size-1){
            if (listCustomer[i].name.equals(name)){
                return i
            }
        }
        return -1
    }
    fun getLastIdCustomer(): Int{
        if (this.listCustomer.size == 0){
            return -1
        }else{
            var idxLast = this.listCustomer.size - 1
            return this.listCustomer[idxLast].id
        }
    }
    fun addMyTransactionOrderCustomer(nameCustomer: String, newTrx: OrderTransaction): Boolean{
        var idx = this.getIndexCustomer(nameCustomer)
        if (idx == -1){
            return false
        }else{
            this.listCustomer[idx].addMyTransactionOrder(newTrx)
            return true
        }
    }
    fun updateStateReturnOfMyTransactionCustomer(nameCustomer: String, newTrx: OrderTransaction):Boolean{
        val index = this.getIndexCustomer(nameCustomer)
        if(index == -1){
            return false
        }else{
            this.listCustomer[index].updateStateTransaction(newTrx.isReturn, newTrx.idOrderTransaction)
            return true
        }
    }
    fun getAllMyOrderTransaction(nameCustomer: String):ArrayList<OrderTransaction>{
        val index = this.getIndexCustomer(nameCustomer)
        if (index == -1){
            return arrayListOf<OrderTransaction>()
        }
        return this.listCustomer[index].listOfMyOrderTransaction
    }
    fun getLastIdMyOrderTransaction(customerName: String): Long{
        var idxCustomer = getIndexCustomer(customerName)
        if(idxCustomer == -1){
            return -1
        }
        else {
            var idxLast = this.listCustomer[idxCustomer].listOfMyOrderTransaction.size - 1
            return this.listOrderTransaction[idxLast].idOrderTransaction

        }
    }

    fun addOwnerRental(name: String, nik: String, address: String){
        if (this.listOwnerRental.size > 0 ) {
            var idLast = this.getLastIdOwnerRental()
            var newOwnerRental = OwnerRental(idLast+1, name, nik, address)
            this.listOwnerRental.add(newOwnerRental)
        }else{
            this.listOwnerRental.add(OwnerRental(0, name, nik, address))
        }
    }
    fun addOwnerRentalObj(ownerr: OwnerRental){
        this.listOwnerRental.add(ownerr)
    }
    fun getOwnerRental(name: String): OwnerRental{
        for(i in this.listOwnerRental){
            if (i.name.equals(name)){
                return i
            }
        }
        return OwnerRental(0, "", "", "")
    }
    fun getOwnerRental(id: Int): OwnerRental{
        for(i in this.listOwnerRental){
            if (i.id == id){
                return i
            }
        }
        return OwnerRental(0,"", "", "")
    }
    fun getIndexOwnerRental(idOwnerRental: Int): Int{
        for(i in 0 .. this.listOwnerRental.size - 1){
            if (this.listOwnerRental[i].id == idOwnerRental){
                return i
            }
        }
        return -1
    }
    fun getLastIdOwnerRental(): Int{
        if (this.listOwnerRental.size == 0){
            return -1
        }else{
            var idxLast = this.listOwnerRental.size - 1
            return this.listOwnerRental[idxLast].id
        }
    }

    fun addBikes(id : Int, model: String, merk: String , bikeType: String, priceOfBuy : Long, priceOfRent : Long, idOwner: Int){
        if(this.listBikes.size > 0){
            var lastId = this.getLastIdBikes()
            this.listBikes.add(Bikes(lastId+1, model, merk, bikeType, priceOfBuy, idOwner))
        }
        var newBikes = Bikes(id, model, merk, bikeType, priceOfBuy, 1)
        this.listBikes.add(newBikes)
    }
    fun addBikesObj(bikess: Bikes){
        this.listBikes.add(bikess)
    }
    fun getLastIdBikes(): Int{
        if (this.listBikes.size == 0){
            return -1
        }else{
            var idxLast = this.listBikes.size - 1
            return this.listBikes[idxLast].id
        }
    }

    fun addOrder(bikes: Bikes, customer: Customer, owner: OwnerRental, time: LocalDateTime, initialDuration: Duration, cost: Long){
        if(this.listOrderTransaction.size > 0) {
            var lastId = this.getLastIdOrderTransaction()
            var newOrderTransaction = OrderTransaction(lastId+1, bikes, customer, owner.id, time, initialDuration, cost)
            this.listOrderTransaction.add(newOrderTransaction)
            var idxOwnerRental = this.getIndexOwnerRental(newOrderTransaction.bikes.idOwner)
            var randomCode = this.generateConfirmationCode()
            var newReturnTransaction = ReturnTransaction(newOrderTransaction, randomCode)
            this.listOwnerRental[idxOwnerRental].addReturnTransaction(newReturnTransaction)
        }else{
            var newOrderTransaction = OrderTransaction(0, bikes, customer, owner.id, time, initialDuration, cost)
            this.listOrderTransaction.add(newOrderTransaction)
            var idxOwnerRental = this.getIndexOwnerRental(newOrderTransaction.bikes.idOwner)
            var randomCode = this.generateConfirmationCode()
            var newReturnTransaction = ReturnTransaction(newOrderTransaction, randomCode)
            this.listOwnerRental[idxOwnerRental].addReturnTransaction(newReturnTransaction)
        }
    }

    fun addOrderObj(orderr : OrderTransaction){
        this.listOrderTransaction.add(orderr)
        var idxOwnerRental = this.getIndexOwnerRental(orderr.bikes.idOwner)
        var randomCode = this.generateConfirmationCode()
        var newReturnTransaction = ReturnTransaction(orderr, randomCode)
        this.listOwnerRental[idxOwnerRental].addReturnTransaction(newReturnTransaction)
    }
    fun getIndexOrderTransaction(idTrx: Long): Int{
        for(i in 0 .. this.listOrderTransaction.size-1){
            if (listOrderTransaction[i].idOrderTransaction == idTrx){
                return i
            }
        }
        return -1
    }
    fun getLastIdOrderTransaction(): Long{
        if (this.listOrderTransaction.size == 0){
            return -1
        }else{
            var idxLast = this.listOrderTransaction.size - 1
            return this.listOrderTransaction[idxLast].idOrderTransaction
        }
    }

    private fun generateConfirmationCode(): String{
        var listArray: ArrayList<Char> = arrayListOf<Char>('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        var confirmationCode = ""
        for (i in 0 .. 16){
            var x = Random.nextInt(until = 26)
            confirmationCode = confirmationCode + listArray[x]
        }
        return confirmationCode
    }
    public fun confirmTheConfirmationCode(code: String, idTrx: Long): Boolean{
        var indexOrderTrx = this.getIndexOrderTransaction(idTrx)
        var orderTrx = this.listOrderTransaction[indexOrderTrx]
        var idOwnerRental = orderTrx.idOwnerRental
        var indexOwnerRental = this.getIndexOwnerRental(idOwnerRental)
        var ownerRental = this.listOwnerRental[indexOwnerRental]
        var isValid = ownerRental.confirmReturn(code)

        when (isValid){
            true -> return true
            else -> return false
        }
    }
}