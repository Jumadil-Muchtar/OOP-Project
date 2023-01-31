import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Duration

class CustomerApp(session: Session) {
    var session: Session = session
    lateinit var bike: Bikes
    lateinit var customer: Customer
    lateinit var ownerRental: OwnerRental
    lateinit var transaction: OrderTransaction

    init {
        this.loginOrRegister()
    }
    fun loginOrRegister(){

        // Customer menginput namanya
        println("Please input your name")
        val nameOfCustomer = readln()
        // Jika namanya ada di List Customer dalam Session, maka Customer akan langsung login
        // Jika tidak, maka Customer harus mendaftar terlebih dahulu dengan menginput nik dan alamatnya
        if(!this.session.isAnyCustomer(nameOfCustomer)){
            println("Please input your NIK")
            var nikOfCustomer = readln()
            println("Please input your address")
            var addressOfCustomer = readln()
            val lastIdCustomer = this.session.getLastIdCustomer()
            var newCustomer = Customer(lastIdCustomer+1, nameOfCustomer, nikOfCustomer, addressOfCustomer)
            this.session.addCustomerObj(newCustomer)
            this.customer = newCustomer
        }else{
            this.customer = this.session.getCustomer(nameOfCustomer)
        }
        println("Hi $nameOfCustomer, Welcome to Bike Rental System.")
        this.chooseMenu()
    }
    fun chooseMenu(){
        println("Your money is Rp ${this.customer.saldo}")
        println("Please select the Menu")
        println("1. Rental Bike")
        println("2. TopUp Saldo")
        println("3. My Order Transaction")
        println("99. Logout")
        println("Please input the number of your option")
        var menuSelected = readln().toInt()

        when(menuSelected){
            1 -> this.choosingBikes()
            2 -> {
                this.customer.topupSaldo()
                this.chooseMenu()
            }
            3 -> this.myOrderTransaction()
            99 -> this.loginOrRegister()
            else -> {
                println("Input is False")
                println()
                this.chooseMenu()
            }
        }
    }


    fun choosingBikes(){
        println("The following are the types of bicycles that we rent")

        var x = 0
        for (i in this.session.listBikes){
            println("${++x}. ${i.model}")
        }
        println("0. Back")
        println("Please input the number of your option")
        var bikeOfChoice = readln().toInt()
        if (bikeOfChoice == 0){
            this.chooseMenu()
        } else if(bikeOfChoice <= this.session.listBikes.size){
            var bikeSelected = this.session.listBikes[bikeOfChoice-1]
            this.bike = bikeSelected
            bikesDetails()
        }else{
            println("Input is false")
            println()
            this.choosingBikes()
        }
    }
    fun bikesDetails(){
        println("Information Details of ${this.bike.model}")
        println()
        println(""" 
            
            Model : ${this.bike.model}
            Merk : ${this.bike.merk}
            
        """.trimIndent())
        println("Would you rental this bike?")
        println("1. Yes")
        println("0. Back")
        println("Please input the number of your option")
        var wantToRental = readln().toInt()
        if (wantToRental == 1){
            this.inputDuration()

        } else if (wantToRental == 0){
            choosingBikes()
        } else{
            println("Input is false")
            println()
            this.bikesDetails()
        }
    }
    fun inputDuration(){
        var priceRentalperWeek = this.bike.priceOfBuy/5
        var priceRentalperDay = this.bike.priceOfBuy/20
        var priceRentalperHour = this.bike.priceOfBuy/100

        println("""
            
                Fee Rental :
                - Rp $priceRentalperHour per hour
                - Rp $priceRentalperDay per day
                - Rp $priceRentalperWeek per week 
                
                """)
        println("Please input duration of your rental.")

        print("Week : ")
        val week = readln().toDouble()
        print("Day : ")
        val day = readln().toDouble()
        print("Hour : ")
        val hour = readln().toDouble()

        var costRental = week*priceRentalperWeek + day*priceRentalperDay + hour*priceRentalperHour
        var costRentall = costRental.toLong()
        println("Cost of this rental : Rp $costRentall,00")
        var durationrental = DurationRental(week.toInt(), day.toInt(), hour.toInt()).durationRental

        payRental(costRentall, durationrental)
    }
    fun payRental(costRental: Long, durationRental : Duration){
        if (costRental == 0L){
            println()
            println("Input rental duration correctly")
            println()
            this.inputDuration()
        }
        var payRentall = this.customer.payRental(costRental)

        try {
            var payRentalll = payRentall.toLong()
            if(payRentalll >= 0){
                this.ownerRental = this.session.getOwnerRental(this.bike.idOwner)
                var newIdTrx= this.session.getLastIdOrderTransaction() + 1
                this.session.getAllMyOrderTransaction(this.customer.name)
                var newOrderTrx = OrderTransaction(newIdTrx, this.bike, this.customer, this.ownerRental.id, LocalDateTime.now(), durationRental, payRentalll)
                this.transaction = newOrderTrx
                this.session.addMyTransactionOrderCustomer(this.customer.name, this.transaction)
                this.session.getAllMyOrderTransaction(this.customer.name)
                this.session.addOrderObj(this.transaction)
                this.getStruckTrx()
            }else{
                println("Your money isn't enough, please topup to rental this bike")
                this.chooseMenu()
            }
        }catch (error: NumberFormatException){
            if(payRentall.equals("Back")){
                this.bikesDetails()
            }else {
                println("Input is mistake")
                this.payRental(costRental, durationRental)
            }
        }
    }

    fun getStruckTrx(){
        println("""
            print struck......
            
            
            STRUK ORDER BIKE RENTAL SYSTEM
            ID TRANSAKSI : ${this.transaction.idOrderTransaction}
            NAMA OWNER RENTAL : ${this.session.getOwnerRental(this.transaction.idOwnerRental).name }
            NAMA CUSTOMER : ${this.customer.name}
            UNIT RENTAL : ${this.bike.model} - ${this.bike.merk}
            TANGGAL RENTAL : ${this.transaction.rentedDateTime}
            DURASI RENTAL : ${this.transaction.initialDuration}
            BIAYA RENTAL : ${this.transaction.cost}
            TANGGAL JATUH TEMPO: ${this.transaction.dueDateTime}
            
            Sisa saldo Anda Rp. ${this.customer.saldo}
            
        """.trimIndent())

        print("""
            Congratulation, you are succeded to order Rental on this system.
            You can immediately use the bike that you have rented
            Keep Enjoy and be careful.
        """.trimIndent())
        println()

        this.chooseMenu()
    }


    // Return Process
    fun myOrderTransaction(){
        var orderTrx = this.session.getAllMyOrderTransaction(this.customer.name)
        println("This is all of your Order Transaction")
        if (orderTrx.size > 0) {
            for (i in 0..orderTrx.size - 1) {
                println("${i + 1}. ${orderTrx[i].bikes.model}-${orderTrx[i].bikes.merk} on ${orderTrx[i].rentedDateTime} (${orderTrx[i].returnState})")
            }
            println("0. Back")
            println("Please input the number of transaction details")
            var nextStep = readln().toInt()
            if(nextStep > 0 && nextStep <= orderTrx.size){
                this.transaction = orderTrx[nextStep-1]
                this.orderTransactionDetails()
            }else if(nextStep == 0){
                this.chooseMenu()
            }else{
                println("your input is mistake")
                this.myOrderTransaction()
            }

        } else{
            println("You don't have rental transaction history")
            println("0. Back")
            println("Please input the number of transaction details")
            var nextStep = readln()

            if(nextStep.equals("0")){
                this.chooseMenu()
            }else{
                println("your input is mistake")
                this.myOrderTransaction()
            }
        }


        fun nextStep(){
            println("Please input the number of transaction details")
            var nextStep = readln().toInt()
            try {
                var nextStepp = nextStep.toInt()

                if(nextStepp == 0){
                    this.chooseMenu()
                }else if(nextStepp > 0 && nextStepp < orderTrx.size){
                    this.transaction = orderTrx[nextStepp-1]
                    this.orderTransactionDetails()
                }else{
                    println("Input is mistake")
                    nextStep()
                }
            }catch (error: NumberFormatException){
                println("Input is mistake")
                nextStep()
            }
        }
    }
    fun orderTransactionDetails(){
        println("Details Transaction")
        println("""
            
            Id : ${this.transaction.idOrderTransaction}
            Owner: ${this.session.getOwnerRental(this.transaction.idOwnerRental).name}
            Unit Bike : ${this.transaction.bikes.model} - ${this.transaction.bikes.merk}
            Rented Date : ${this.transaction.rentedDateTime}
            Duration : ${this.transaction.initialDuration}
            Cost : ${this.transaction.cost}
            Due Date: ${this.transaction.dueDateTime}
            State : ${this.transaction.returnState}
            
        """.trimIndent())


        if(this.transaction.isReturn){
            println("The Bike is returned, what the next?")
            println("1. Menu")
            println("0. Back")
            println("Please input the number of transaction details")

            var nextStep = readln()
            when(nextStep){
                "1" -> this.chooseMenu()
                "0" -> this.myOrderTransaction()
                else->{
                    println("Input is Mistake")
                    println()
                    this.orderTransactionDetails()
                }
            }
            this.myOrderTransaction()
        }else{
            println("The Bike is not yet returned, Do you return it, Now?")
            println("1. Yes")
            println("2. Menu")
            println("0. Back")
            println("Please input the number of transaction details")
            var nextStep = readln()
            when(nextStep){
                "1" -> this.returnBikes()
                "2" -> this.chooseMenu()
                "0" -> this.myOrderTransaction()
                else->{
                    println("Input is Mistake")
                    this.orderTransactionDetails()
                }
            }
        }

    }
    fun returnBikes(){
        println("Please enter the confirmation code provided by the Owner Rental")
        println("Code : ${hintCode(this.transaction.idOrderTransaction)}")
        var confirmationCode = readln().toString()
        println("Waiting the owner to confirmation your return.")
        var confirmed = this.session.confirmTheConfirmationCode(confirmationCode, this.transaction.idOrderTransaction)

        try{
            TimeUnit.SECONDS.sleep(10)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

        if (confirmed){
            var indexOrderTrx = this.session.getIndexOrderTransaction(this.transaction.idOrderTransaction)
            var isReturn = this.session.listOrderTransaction[indexOrderTrx].returnBike()
            if (isReturn) {
                println(
                    """
                
                Return process have been confirmed by owner rental.
                Thank you for rental our bike, we will always be there when you need them again.
                
            """.trimIndent()
                )
            }
            this.myOrderTransaction()
        } else {
            println("""
                
                The owner has never received a return of the bicycle that you rented.
                Please return it first then ask for confirmation from the owner through this system.
                
            """.trimIndent())

            println("1. Confirm Again")
            println("2. Menu")
            println("0. Back")
            println("Please input the number of transaction details")
            var nextStep = readln()
            when(nextStep){
                "1" -> this.returnBikes()
                "2" -> this.chooseMenu()
                "0" -> this.orderTransactionDetails()
                else->{
                    println("Input is Mistake")
                    this.orderTransactionDetails()
                }
            }
        }
    }
    fun hintCode(idOrderTrx: Long): String{
        return this.ownerRental.hackCodeConfirmation(idOrderTrx)
    }
}