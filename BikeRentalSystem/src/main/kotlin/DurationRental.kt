
import java.time.Duration


open class DurationRental(week: Long, day: Long, hour: Long){
    private var week: Long = week
    private var day: Long = day
    private var hour: Long = hour
    var durationRental : Duration = Duration.ZERO
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

        var totalDay = this.day + this.day * 7
        var hourr = Duration.ofHours(this.hour)
        this.durationRental = hourr.plusDays(totalDay)
    }
    constructor(day: Long, hour: Long): this(0,day, hour)
    constructor(hour: Long): this(0, 0,hour)
    constructor(duration: Duration) : this(duration.toDaysPart(), duration.toHoursPart().toLong())
    public fun printDuration(){
        println("Abstract Value : ${this.durationRental.abs()}")
    }
    public fun compareTo(duration: Duration): Int{
        return this.durationRental.compareTo(duration)
    }

    public fun getDurationRental(): String{
        return "${this.week} week ${this.day} day ${this.hour} hour"
    }


}