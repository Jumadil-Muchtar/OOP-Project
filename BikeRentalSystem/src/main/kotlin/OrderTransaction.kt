
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import java.time.Duration as JavaDuration

class OrderTransaction(id:Long, bikes: Bikes, customer: Customer, idOwner: Int, time: LocalDateTime, initialDuration: Duration, cost: Long){
    val idOrderTransaction: Long = id
    var customer: Customer = customer
    var idOwnerRental: Int = idOwner
    var bikes: Bikes = bikes
    val cost: Long = cost
    var rentedDateTime: LocalDateTime = time
    var isPayed: Boolean = false
    var initialDuration :Duration = initialDuration
    lateinit var realDuration : JavaDuration
    var isReturn : Boolean = false
    var returnState : String = "Not yet returned"
    lateinit var dueDateTime : LocalDateTime
        private set
    lateinit var returnDateTime: LocalDateTime
    // List Fine in Percentage
    var listFine: ArrayList<Double> =  arrayListOf<Double>(0.25,0.06, 0.02, 0.005)
    var totalFine: Long = 0
    init {
        println("Telah terjadi transaksi dengan id ${this.idOrderTransaction} penyewaan sepeda ${bikes.model} merek ${bikes.merk} oleh customer atas nama ${customer.name} kepada penyewa dengan id ${this.idOwnerRental}")
        calculateDueDateTime()
    }
    fun calculateDueDateTime(){
        var localDateTime = this.rentedDateTime
        var newDateTime = localDateTime.plusSeconds(this.initialDuration.inWholeSeconds)
        this.dueDateTime =  newDateTime
    }
    fun returnBike(): Boolean{
        if (this.isReturnLate()){
            var durationLate = this.realDuration.minus(this.initialDuration.toJavaDuration())
            this.calculateFine(durationLate)
            println("You were fined for being late to return our bikes. You have to pay as much as Rp. ${this.totalFine}")
            return false
        }
        this.editReturnState(true)
        return true
    }
    private fun editReturnState(isReturn: Boolean){
        this.isReturn = isReturn
        this.returnState = "Already returned"
    }

    fun isReturnLate(): Boolean{
        this.returnDateTime = LocalDateTime.now()
        this.realDuration = JavaDuration.between(this.rentedDateTime, this.returnDateTime)
        var comparing = this.realDuration.compareTo(this.initialDuration.toJavaDuration())
        when (comparing) {
            1 -> return true

            -1 -> {
                println("You returned it earlier")
                return false
            }
            0 -> {
                println("You returned it on time")
                return false
            }
            else -> {
                println("Error")
                return false
            }
        }
    }
    fun calculateFine(durationLate: JavaDuration){
        var numberOfWeeks = durationLate.toDaysPart() / 7
        var numberOfDays = durationLate.toDaysPart()
        var numberOfHours = durationLate.toHoursPart()
        var numberOfMinute = durationLate.toMinutesPart()
        var priceBikes = this.bikes.priceOfBuy
        var totalOfFine = numberOfWeeks*this.listFine[0]*priceBikes + numberOfDays*this.listFine[1]*priceBikes + numberOfHours*this.listFine[2]*priceBikes

        if(numberOfMinute>= 30){
            totalOfFine +=  this.listFine[3]*priceBikes
        }
        this.totalFine = totalOfFine.toLong()
    }

}