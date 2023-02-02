import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
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
        println("---------- Login Or Register ----------")

        // Customer menginput namanya
        println("Please input your name")
        print(">> ")
        val nameOfCustomer = readln()
        // Jika namanya ada di List Customer dalam Session, maka Customer akan langsung login
        // Jika tidak, maka Customer harus mendaftar terlebih dahulu dengan menginput nik dan alamatnya
        if(!this.session.isAnyCustomer(nameOfCustomer)){
            println("Please input your NIK")
            print(">> ")
            var nikOfCustomer = readln()
            println("Please input your address")
            print(">> ")
            var addressOfCustomer = readln()
            val lastIdCustomer = this.session.getLastIdCustomer()
            var newCustomer = Customer(lastIdCustomer+1, nameOfCustomer, nikOfCustomer, addressOfCustomer)
            // Menyimpan Customer ke dalam Session
            this.session.addCustomerObj(newCustomer)
            this.customer = newCustomer
        }else{
            this.customer = this.session.getCustomer(nameOfCustomer)
        }

//        println("---------- Login Or Register ----------")

        // Mengarahkan Customer ke Main Menu
        this.mainMenu()
    }
    fun mainMenu(){
        println("++++++++++ MAIN MENU ++++++++++")

        println()
        println("Hi ${this.customer.name}, Welcome to Bike Rental System.")
        // Menampilkan sisa saldo dari Customer tersebut
        println("Your money is Rp ${this.customer.saldo}")
        println()
        // Memunculkan menu-menu yang tersedia pada aplikasi Bike Rental System di sisi pelanggan (Customer)
        println("Please select the Menu")
        println("1. Rental Bike")
        println("2. Topup Money")
        println("3. My Order Transaction")
        println("99. Logout")
        println("Please input the number of your option")

        // Customer menginputkan Angka dari opsi yang dipilih
        print(">> ")
        var menuSelected = readln().toInt()

//        println("++++++++++ MAIN MENU ++++++++++")
        println()
        when(menuSelected){
            // Ketika menu yang dipilih yaitu '1. Rental Bike' maka Customer akan di tawarkan untuk memilih sepeda yang ingin disewa
            1 -> this.choosingBike()
            // Ketika menu yang dipilih yaitu '2. Topup Money' maka Customer akan ditawarkan untuk melakukan topup saldo (uang digital)
            // Kemudian dikembalikan ke daftar menu aplikasi ini (menu utama)
            2 -> {

                this.topupMoney()
                this.mainMenu()
            }
            // Ketika menu yang dipilih yaitu '3. My Order Transaction' maka Customer akan dibawa ke menu menu My Order Transaction
            // menu ini akan menampilkan seluruh transaksi rental yang pernah dilakukannya
            3 -> this.myOrderTransaction()
            // Ketika menu yang dipilih yaitu '4. Logout' maka customer akan di bawah ke mwnu Login
            99 -> this.loginOrRegister()
            // Ketika Customer menginput selain dari keempat angka di atas maka Customer akan diminta kembali menginput angka tersebut.
            else -> {
                println("Input is False")
                println()
                this.mainMenu()
            }
        }
    }

    fun topupMoney() {
        println("++++++++++ TOPUP MONEY ++++++++++")
        this.customer.topupMoney()
//        println("++++++++++ TOPUP MONEY ++++++++++")
    }

    fun choosingBike(){
        println("---------- Choosing Bike --------")

        // Menampilkan seluruh daftar sepeda yang dapat disewa di platform ini
        println("The following are the types of bicycles that we rent")
        var x = 0
        for (i in this.session.listBikes){
            println("${++x}. ${i.model}")
        }
        println("0. Back")

        // Customer menginputkan angka dari opsi yang dipilih
        println("Please input the number of your option")
        print(">> ")
        var bikeOfChoice = readln().toInt()

//        println("---------- Choosing Bike --------")

        // Jika Customer memilih opsi '0. Back' maka customer akan di bawa ke menu sebelumnya yaitu menu utama
        if (bikeOfChoice == 0){
            this.mainMenu()
        }
        // Jika Customer memilih salah satu model sepeda, maka Customer akan di bawa ke sub menu Bike Details (rincian sepeda)
        else if(bikeOfChoice <= this.session.listBikes.size){
            var bikeSelected = this.session.listBikes[bikeOfChoice-1]
            this.bike = bikeSelected
            bikeDetails()
        }
        // Jika pilihan Customer tidak ada pada salah satu model sepeda yang ditawarkan maka input dinyatakan salah
        // Selanjutnya Customer akan di arahkan kembali untuk memilih sepeda yang ingin disewa
        else{
            println("Input is false")
            println()
            this.choosingBike()
        }
    }
    fun bikeDetails(){
        println("---------- Bike Details --------")

        // Menampilkan informasi dari sepeda yang dipilih secara rinci seperti model, dan merk
        println("Information Details of ${this.bike.model}")
        println(""" 
            
            Model : ${this.bike.model}
            Merk : ${this.bike.merk}
            
        """.trimIndent())
        println("Would you rental this bike?")
        println("1. Yes")
        println("0. Back")

        // Customer menentukan apakah akan menyewa sepeda itu atau tidak
        // Jika Customer memilih opsi '1. Yes' maka Customer akan diarahkan untuk menentukan durasi penyewaan
        // Jika Customer memiloh opsi '0. Back' maka Customer akan dikembalikan ke sub menu sebelumnya yaitu Choosing Bike

        println("Please input the number of your option")
        print(">> ")
        var wantToRental = readln().toInt()
        if (wantToRental == 1){
            this.inputDuration()
        } else if (wantToRental == 0){
            choosingBike()
        } else{
            println("Input is false")
            println()
            this.bikeDetails()
        }
    }
    fun inputDuration(){
        println("---------- Input Duration -----------")
        // Customer akan diperlihatkan biaya penyewaan sepeda tersebut dalam periode waktu per jam, per hari atau per minggu

        var priceRentalperWeek = this.bike.priceOfBuy/5
        var priceRentalperDay = this.bike.priceOfBuy/20
        var priceRentalperHour = this.bike.priceOfBuy/100

        println("""
            
                Fee Rental :
                - Rp $priceRentalperHour per hour
                - Rp $priceRentalperDay per day
                - Rp $priceRentalperWeek per week 
                
                """)

        // Customer memasukkan jumlah minggu, jumlah hari dan jumlah jam penyewaan sepeda
        // Namun Customer tidak dapat mengosongkan ketiga kolom tersbut
        // Customer bisa mengisi angka 0 apabila tidak ingin menyewa sepeda dalam periode itu
        println("Please input duration of your rental.")

        print("Week : ")
        val week = readln().toDouble()
        print("Day : ")
        val day = readln().toDouble()
        print("Hour : ")
        val hour = readln().toDouble()
        // Aplikasi Customer melakukan kalkulasi jumlah biaya sewa yang harus dibayarkan.
        // Kalkulasi ini berdasarkan jumlah setiap priode dengan biaya sewa setiap periode
        var costRental = week*priceRentalperWeek + day*priceRentalperDay + hour*priceRentalperHour
        var costRentall = costRental.toLong()
        // Menampilkan biaya rental kepada Customer
        println("Cost of this rental : Rp $costRentall,00")


        // Kemudian melakukan konversi dari jumlah setiap periode penyewaan ke objek Duration yang merupakan standard libray dari Kotlin.
        var durationrental = DurationRental(week.toInt(), day.toInt(), hour.toInt()).durationRental

//        println("---------- Input Duration ----------")
        // Mengarahkan Customer ke sub menu Pay Rental
        payRental(costRentall, durationrental)
    }
    fun payRental(costRental: Long, durationRental : Duration){
        // Aplikasi Customer akan memeriksa biaya penyewaan sepeda
        // Jika biaya penyewaan sepeda = 0, artinya durasi penyewaan yang diinput Customer tidak valid
        // Maka Customer akan diarahkan untuk menginput kembali durasi penyewaannya

        if (costRental <= 0L){
            println()
            println("Input rental duration correctly")
            println()
            this.inputDuration()
        }

        println("----------- Pay Rental -----------")

        // Jika tidak (biaya penyewaan > 0), maka konfirmasi pembayaran kepada Customer melalui fungsi payRental(costRental: Long)
        // Fungsi ini akan mengembalikan sebuah String. Jika String tersebut dapat dikonversi ke tipe data Long maka perintah dalam scope try akan diteruskan.
        // Jika string tersebut tidak dapat dikonversi ke tipe data Long, maka akan terjadi error bernama NumberFormatException.
        // Selanjutnya, error ini di tangkap (catch) kemudian perintah didalam scope catch dijalankan.
        // Untuk lebih detailnya tentang fungsi ini, Anda dapat membukanya di class Customer pada file Customer.kt

        // variabel payDecision menentukan apakah Customer membayar biaya sewa
        var payDecision = this.customer.payRental(costRental)

        try {
            // Melakukan Konversi nilai dari variabel payRentall yang bertipe data String ke tipe Data Long
            var remainingMoney = payDecision.toLong()
            // Variabel remainingMoney berisi sisa uang yang dimiliki Customer setelah membayar biaya sewa
            // Jika konversi berhasil dilakukan, maka perintah dibawah dapat di eksekusi
            // Jika payRentalll >= 0 artinya Saldo Customer masih tersisa atau sama dengan 0 setelah membayar biaya sewa
            // Maka Order Transaction akan dibuat
            if(remainingMoney >= 0){
                // Mendapatkan objek OwnerRental dari Session (Penyimpanan Sementara Aplikasi ini)
                this.ownerRental = this.session.getOwnerRental(this.bike.idOwner)
                // Membuat id OrderTransaksi baru dari transaksi order terakhir yang ditambah 1
                var newIdTrx= this.session.getLastIdOrderTransaction() + 1
                // Membuat objek OrderTransaction
                var newOrderTrx = OrderTransaction(newIdTrx, this.bike, this.customer, this.ownerRental.id, LocalDateTime.now(), durationRental, remainingMoney)
                // Menyimpan objek newOrderTrx ke property transaction class CustomerApp
                this.transaction = newOrderTrx
                // Menyimpan object (data) OrderTransaction ke dalam daftar MyTransaction Customer tersebut di Session.
                this.session.addMyTransactionOrderCustomer(this.customer.name, this.transaction)
                // Menyimpan object (data) OrderTransaction ke dalam daftar OrderTransaction  di Session.
                this.session.addOrderObj(this.transaction)
                // Mengalihkan Customer ke tampilan Struk Order Transaksi
                this.getStruckTrx()
//                println("----------- Pay Rental -----------")
                println()
            }
            // Jika variabel remainingMoney bernilai negatif, artinya Saldo Customer belum mencukupi untuk menyewa sepeda tersebut dengan durasi yang telah ditentukan.
            // Maka kembalikan tampilan aplikasi Customer ke Main Menu
            else{
                println("Your money isn't enough, please topup to rental this bike")
//                println("----------- Pay Rental -----------")
                println()
                this.mainMenu()
            }
        }catch (error: NumberFormatException){
            // Jika error berhasil ditangkap, maka perintah dibawah akan dijalankan
            // Jika variabel payDecision berisi String "Back", artinya Customer memilih untuk kembali ke sub menu sebelumnya yaitu Input Duration.
            if(payDecision.equals("Back")){
//                println("----------- Pay Rental -----------")
                println()
                this.bikeDetails()
            }
            // Jika tidak, artinya customer salah input. Maka Customer diarahkan untuk melakukan pembayaran biaya sewa
            else {
                println("Input is mistake")
//                println("----------- Pay Rental -----------")
                println()
                this.payRental(costRental, durationRental)
            }
        }
    }

    fun getStruckTrx(){
        // Memperlihatkan Struk Order Transakaksi yang telah berhasil dilakukan.
        println("""
            print struck......
            
            
            STRUK ORDER BIKE RENTAL SYSTEM
            ID TRANSAKSI : ${this.transaction.idOrderTransaction}
            NAMA OWNER RENTAL : ${this.session.getOwnerRental(this.transaction.idOwnerRental).name }
            NAMA CUSTOMER : ${this.customer.name}
            UNIT RENTAL : ${this.bike.model} - ${this.bike.merk}
            TANGGAL RENTAL : ${this.transaction.rentedDateTime}
            DURASI RENTAL : ${this.transaction.getDurationRental()}
            BIAYA RENTAL : ${this.transaction.cost}
            TANGGAL JATUH TEMPO: ${this.transaction.dueDateTime}
            
            Sisa saldo Anda Rp. ${this.customer.saldo}
            
        """.trimIndent())

        // Menampilkan pesan transaksi order sukses kepada Customer

        print("""
            Congratulation, you are succeded to order Rental on this system.
            You can immediately use the bike that you have rented
            Keep Enjoy and be careful.
        """.trimIndent())
        println()

        // Mengembalikan tampilan aplikasi customer ke Main Menu
        this.mainMenu()
    }


    // Return Process
    fun myOrderTransaction(){
        println("---------- My Order Transaction ----------")

        // Sub menu ini berisi seluruh order transaction (yang sukses) yang telah dilakukan oleh customer tersebut

        // Mendapatkan daftar MyOrderTransaction yang telah dilakukan oleh customer tersebut
        var orderTrx = this.session.getAllMyOrderTransaction(this.customer.name)
        println("This is all of your Order Transaction")
        // Jika daftar MyOrderTransaction berisi objeck OrderTransaction maka tampilkan seluruh riwayat OrderTransaction
        if (orderTrx.size > 0) {
            for (i in 0..orderTrx.size - 1) {
                println("${i + 1}. ${orderTrx[i].bikes.model}-${orderTrx[i].bikes.merk} on ${orderTrx[i].rentedDateTime} (${orderTrx[i].returnState})")
            }
            println("0. Back")

            // Customer kemudian diarahkan untuk memilih OrderTransaction yang ingin ditinjau
            println("Please input the number of transaction details")
            try {
                print(">> ")
                var nextStep = readln().toInt()
                // Jika yang dipilih adalah salah satu dari transaksi yang ditampilkan
                // maka tampilan aplikasi customer diarahkan ke sub menu My Order Transaction
                if(nextStep > 0 && nextStep <= orderTrx.size){
                    this.transaction = orderTrx[nextStep-1]
                    this.orderTransactionDetails()
                }
                // Jika Customer menginput '0. Back' maka kembalikan ke tampilan Main Menu
                else if(nextStep == 0){
                    this.mainMenu()
                }
                // Jika tidak, maka input Customer dinyatakan salah
                else{
                    println("Your input is mistake")
                    this.myOrderTransaction()
                }
            } catch (error: NumberFormatException){
                // Jika konversi gagal dilakukan, input dinyatakan salah
                // Customer akan diminta untuk memilih OrderTransaction yang ingin ditinjau
                println("Your input is mistake")
                this.myOrderTransaction()
            }
//            println("---------- My Order Transaction ----------")
        }
        // Jika MyOrderTransaction tidak berisi objek OrderTransaction artinya Customer tersebut belum pernah melakukan penyewaan sepeda
        // Customer akan diarahkan untuk kembali ke menu sebelumnya yaitu Main Menu
        else{
            println("You don't have rental transaction history")
            println("0. Back")
            println("Please input the number of transaction details")
            print(">> ")
            var nextStep = readln()

            if(nextStep.equals("0")){
//                println("---------- My Order Transaction ----------")
                this.mainMenu()
            }else{
                println("your input is mistake")
//                println("---------- My Order Transaction ----------")
                this.myOrderTransaction()
            }
        }


        fun nextStep(){
            println("Please input the number of transaction details")
            print(">> ")
            var nextStep = readln().toInt()
            try {
                var nextStepp = nextStep.toInt()

                if(nextStepp == 0){
                    this.mainMenu()
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
        println("---------- Details Order Transaction ----------")
        // Sub menu ini akan menampilkan order transaction secara rinci seperti:
        // - id order transaksi
        // - owner rental (sepeda)
        // - Model dan merk sepeda yang disewa
        // - Tanggal penyewaan
        // - Durasi penyewaan
        // - Biaya penyewaan
        // - Tanggal Jatuh Tempo / Berakhirnya penyewaan
        // - Status penyewaan (Apakah sepeda sudah dikembalikan atau belum)

        println("""
            
            Id : ${this.transaction.idOrderTransaction}
            Owner: ${this.session.getOwnerRental(this.transaction.idOwnerRental).name}
            Unit Bike : ${this.transaction.bikes.model} - ${this.transaction.bikes.merk}
            Rented Date : ${this.transaction.rentedDateTime}
            Duration : ${this.transaction.getDurationRental()}
            Cost : ${this.transaction.cost}
            Due Date: ${this.transaction.dueDateTime}
            State : ${this.transaction.returnState}
            
        """.trimIndent())

        // Aplikasi mengecek apakah customer sudah mengembalikan sepeda yang disewanya
        // Jika sudah dikembalikan maka Customer diarahkan apakah ingin kembali ke menu sebelumnya yaitu My Order Transaction atau kembali ke Main Menu
        if(this.transaction.isReturn){
            println("The Bike is returned, what the next?")
            println("1. Main Menu")
            println("0. Back")
            println("Please input the number of transaction details")
            print(">> ")
            var nextStep = readln()
            when(nextStep){
                "1" -> {
//                    println("---------- Details Order Transaction ----------")
                    this.mainMenu()
                }
                "0" -> {
//                    println("---------- Details Order Transaction ----------")
                    this.myOrderTransaction()
                }
                else->{
                    println("Input is Mistake")
//                    println("---------- Details Order Transaction ----------")
                    println()
                    this.orderTransactionDetails()
                }
            }
        }
        // Jika customer belum mengembalikan sepeda yang disewanya, maka customer akan ditawarkan untuk mengembalikan sepeda tersebut
        else{
            println("The Bike is not yet returned, Do you return it, Now?")
            println("1. Yes")
            println("2. Main Menu")
            println("0. Back")
            println("Please input the number of transaction details")
            print(">> ")
            var nextStep = readln()


            // Ketika Customer memilih '1. Yes' , maka tampilan aplikasi Customer akan dibawa ke sub menu Return Bike
            // Ketika Customer memilih '2. Main Menu', maka tampilan aplikasi Customer akan dibawa ke Main Menu
            // Ketika Customer memilih '0. Back', maka tampilan aplikasi Customer akan dibawa ke sub menu sebelumnya yaitu My Order Transaction
            // Ketika inputan tidak memenuhi ketiganya, maka inputan dianggap salah
            when(nextStep){
                "1" -> {
//                    println("---------- Details Order Transaction ----------")
                    println()
                    this.returnBike()
                }
                "2" -> {
//                    println("---------- Details Order Transaction ----------")
                    println()
                    this.mainMenu()
                }
                "0" -> this.myOrderTransaction()
                else->{
                    println("Input is Mistake")
//                    println("---------- Details Order Transaction ----------")
                    println()
                    this.orderTransactionDetails()
                }
            }
        }

    }
    fun returnBike(){
        println("----------- Return Bike ----------")

        // Ini adalah sub menu Return Bike
        // Di sub menu ini, Customer akan mengisi kode konfirmasi yang diberikan oleh owner rental.
        // Kode konfirmasi ini menjadi tanda bukti bahwa Owner Rental telah menerima sepeda dari Customer.
        // Sehingga dianjurkan bagi para owner rental untuk memberikan kode konfirmasi ketika Customer telah mengembalikan sepeda yang disewakannya.

        println("Please enter the confirmation code provided by the Owner Rental")
        println("Code : ${hintCode(this.transaction.idOrderTransaction)}")
        print(">> ")
        var confirmationCode = readln().toString()
        println("Waiting the owner to confirmation your return.")

        //  variabel confirmed berisi Boolean yang menyatakan apakah kode yang dimasukkan Customer valid atau tidak.
        var confirmed = this.session.confirmTheConfirmationCode(confirmationCode, this.transaction.idOrderTransaction)

        // Hitung mundur 10 detik sebagai tanda bahwa sistem lagi mengkonfirmasi apakah code yang dimasukkan valid atau tidak

        try{
            TimeUnit.SECONDS.sleep(10)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

        // Jika kode yang dimasukkan valid, maka transaksi akan dianggap berhasi.
        // Sehingga objek OrderTransaction terkait yang ada di Session, statusnya diubah menjadi sudah dikembalikan

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
                println()
//                println("----------- Return Bike ----------")
            }else{
                println("")
            }

            this.myOrderTransaction()
        }
        // Jika kode konfirmasi tidak valid, maka Customer ditawarkan untuk melakukan konfirmasi kembali,
        // kembali ke Main Menu atau kembali ke sub menu sebelumnya yaitu Order Details
        else {
            println("""
                
                The owner has never received a return of the bicycle that you rented.
                Please return it first then ask for confirmation from the owner through this system.
                
            """.trimIndent())

            println("1. Confirm Again")
            println("2. Main Menu")
            println("0. Back")
            println("Please input the number of transaction details")
            print(">> ")
            var nextStep = readln()
            when(nextStep){
                "1" -> this.returnBike()
                "2" -> this.mainMenu()
                "0" -> this.orderTransactionDetails()
                else->{
                    println("Input is Mistake")
                    this.orderTransactionDetails()
                }
            }
        }
    }
    fun hintCode(idOrderTrx: Long): String{
        // Mendapatkan kode konfirmasi secara rahasia
        return this.ownerRental.hackCodeConfirmation(idOrderTrx)
    }
}