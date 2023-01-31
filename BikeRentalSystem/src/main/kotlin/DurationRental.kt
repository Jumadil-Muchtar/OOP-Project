
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


class DurationRental(week: Int, day: Int, hour: Int){
    private val week: Int = week
    private val day: Int = day
    private val hour: Int = hour
    var durationRental : Duration = Duration.INFINITE
        private set

    init {
        var weekk = (week*7).toDuration(DurationUnit.DAYS)
        var dayy = day.toDuration(DurationUnit.DAYS)
        var hourr = hour.toDuration(DurationUnit.HOURS)
        this.durationRental = weekk.plus(dayy).plus(hourr)
    }
    public fun printDuration(){
        println("Abstract Value : ${this.durationRental.absoluteValue}")
    }
    public fun compareTo(duration: Duration): Int{
        return this.durationRental.compareTo(duration)
    }

    constructor(day: Int, hour: Int): this(0,day, hour)
    constructor(hour: Int): this(0, 0,hour)




}