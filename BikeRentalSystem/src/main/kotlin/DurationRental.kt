
import kotlin.time.*


open class DurationRental(week: Int, day: Int, hour: Int){
    private var week: Int = week
    private var day: Int = day
    private var hour: Int = hour
    var durationRental : Duration = Duration.INFINITE
        private set

    init {
        if (this.day > 7){
            this.week = this.week + this.day / 7
            this.day = this.day % 7
        }
        if(this.hour > 24){
            this.day = this.day + this.hour / 24
            this.hour = this.hour % 24
        }

        var weekk = (week*7).toDuration(DurationUnit.DAYS)
        var dayy = day.toDuration(DurationUnit.DAYS)
        var hourr = hour.toDuration(DurationUnit.HOURS)
        this.durationRental = weekk.plus(dayy).plus(hourr)
    }
    constructor(day: Int, hour: Int): this(0,day, hour)
    constructor(hour: Int): this(0, 0,hour)
    constructor(duration: Duration) : this(duration.toJavaDuration().toDaysPart().toInt(), duration.toJavaDuration().toHoursPart().toInt())
    public fun printDuration(){
        println("Abstract Value : ${this.durationRental.absoluteValue}")
    }
    public fun compareTo(duration: Duration): Int{
        return this.durationRental.compareTo(duration)
    }

    public fun getDurationRental(): String{
        return "${this.week} week ${this.day} day ${this.hour} hour"
    }


}