import java.time.LocalDateTime
import java.time.Month
import kotlin.random.Random
import kotlin.time.Duration
import java.time.Duration as JavaDuration

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toKotlinDuration

fun initData(session: Session) : Session{
    val customer: ArrayList<Customer> = arrayListOf(
        Customer(0,"Andi", "7314111111111111", "Jalan Perintis Kemerdekaan No.3, Makassar"),
        Customer(1,"Budi", "7314111111111112", "Jalan Metro Tanjung Bunga No.2, Makassar"),
        Customer(2,"Citra", "7314111111111113", "Jalan Jalur Lingkar Barat No.1, Makassar"),

    )


    val ownerRental: ArrayList<OwnerRental> = arrayListOf(
        OwnerRental(0,"Sinar", "7314111111111112", "Jalan Bambu Runcing, Arateng")
    )

    val bikes: ArrayList<Bikes> = arrayListOf(
        Bikes(0, "COLLOSUS N9", "POLYGON", "ENDURO", 52432515, 0),
        Bikes(1, "SISKIU T7", "POLYGON", "TRAIL", 3200000, 0),
        Bikes(2, "SISKIU D5", "POLYGON", "CROSS COUNTRY", 17967015, 0),
        Bikes(3, "SYNCLINE C2", "POLYGON", "CROSS COUNTRY", 20214765, 0),
        Bikes(4, "SYNCLINE C3", "POLYGON", "CROSS COUNTRY", 23211765, 0),
        Bikes(5, "XTRADA 5", "POLYGON", "CROSS COUNTRY", 11973015, 0)

    )

    val order: ArrayList<OrderTransaction> = arrayListOf()

    for (i in customer){
        session.addCustomerObj(i)
    }
    for (j in ownerRental){
        session.addOwnerRentalObj(j)
    }
    for (k in bikes){
        session.addBikesObj(k)
    }
    for (l in order){
        session.addOrderObj(l)
    }
    return session
}
fun main(args: Array<String>) {
    var session1 = Session()
    initData(session1)
    var customerApp = CustomerApp(session1)

}