package kt.speedy.toolbox

import kt.speedy.toolbox.util.*
import org.joda.time.DateTime
import kotlin.test.Test

class DateTimeTest {
    @Test
    fun test1() {
        println(DateTime.now().startOfDay())
        println(DateTime.now().endOfDay())

        println("-----")
        println(DateTime.now().startOfWeek())
        println(DateTime.now().endOfWeek())

        println("-----")
        println(DateTime.now().startOfMonth())
        println(DateTime.now().endOfMonth())

        println("-----")
        println(DateTime.now().startOfYear())
        println(DateTime.now().endOfYear())
    }

    @Test
    fun test2() {
        val start = "2025-03-01"
        val end = "2025-02-20"
        println(start.toDateTime().getMonthsBetween(end.toDateTime()))
    }
}