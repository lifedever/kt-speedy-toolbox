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
}