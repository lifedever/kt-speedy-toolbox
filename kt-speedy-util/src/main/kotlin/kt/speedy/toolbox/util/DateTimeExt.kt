package kt.speedy.toolbox.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class DateTimeExt {
    companion object {
        fun parse(dateTime: String, pattern: String): DateTime {
            return DateTime.parse(dateTime, DateTimeFormat.forPattern(pattern))
        }

        /**
         * 今天
         */
        fun today(): Date {
            return DateTime.now().toDate()
        }

        fun todayUTC(): Date {
            return org.joda.time.DateTime.now(org.joda.time.DateTimeZone.forTimeZone(java.util.TimeZone.getTimeZone("UTC")))
                .toDate()
        }
    }
}

/**
 * 获取一天的最开始
 */
fun DateTime.startOfDay(): DateTime {
    return this.millisOfDay().withMinimumValue()
}

/**
 * 指定某一天
 * @param day 如：01、15
 */
fun DateTime.toDay(day: String): DateTime {
    return this.toString("yyyy-MM-${day}").toDateTime()
}

/**
 * 获取一天的结束
 */
fun DateTime.endOfDay(): DateTime {
    return this.millisOfDay().withMaximumValue()
}

/**
 * 去除日期，只留时间
 */
fun DateTime.onlyTime(): DateTime {
    return DateTimeExt.parse(this.toString("HH:mm"), "HH:mm")
}

/**
 * 去除时间，只留日期
 */
fun DateTime.onlyDate(): DateTime {
    return DateTimeExt.parse(this.toString("yyyy-MM-dd"), "yyyy-MM-dd")
}

fun DateTime.startOfMonth(): DateTime {
    return this.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue()
}

fun DateTime.endOfMonth(): DateTime {
    return this.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue()
}

/**
 * 获取当前时间的utc时区值
 */

fun Date.onlyTime(): Date {
    return DateTime(this).onlyTime().toDate()
}

fun Date.startOfMonth(): Date {
    return this.toDateTime().startOfMonth().toDate()
}

fun Date.endOfMonth(): Date {
    return this.toDateTime().endOfMonth().toDate()
}

fun Date.onlyDate(): Date {
    return DateTime(this).onlyDate().toDate()
}

fun Date.format(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(pattern).format(this)
}

fun Date.formatMonth(pattern: String = "yyyy-MM"): String {
    return this.format(pattern)
}

fun DateTime.formatMonth(pattern: String = "yyyy-MM"): String {
    return this.toString(pattern)
}

fun Date.toDateTime(): DateTime = DateTime(this)
fun Date.toDateTime(zone: TimeZone): DateTime = DateTime(this, DateTimeZone.forTimeZone(zone))
fun Date.toDateTimeUTC(): DateTime = DateTime(this, DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))
fun Date.toSqlDate(): java.sql.Date = java.sql.Date(this.time)

/**
 * 转换为Date
 */
fun java.time.LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

/**
 * 转换为Date
 */
fun LocalDateTime.toDate(): Date {
    return Date.from(this.atZone(java.time.ZoneId.systemDefault()).toInstant())
}

/**
 * 判断是否为闰年
 */
fun Int.isLeapYear(): Boolean {
    return (this % 4 == 0 && this % 100 != 0) || (this % 400 == 0)
}

/**
 * 获取截止到指定日期为止的月份集合
 * @param count 返回几个月
 */
fun Date.getMonthsForDate(count: Int = 6): List<Date> {
    return (1..count).map {
        this.toDateTime().minusMonths(it - 1).toDate()
    }.reversed()
}

/**
 * 获取截止到指定日期为止的日期集合
 * @param count 返回几天
 */
fun Date.getDaysForDate(count: Int = 6): List<Date> {
    return (1..count).map {
        this.toDateTime().minusDays(it - 1).toDate()
    }.reversed()
}

/**
 * 获取截止到指定日期为止的周集合
 * @param count 返回几周
 */
fun Date.getWeekForDate(count: Int = 6): List<Date> {
    return (1..count).map {
        this.toDateTime().minusWeeks(it - 1).toDate()
    }.reversed()
}

/**
 * 获取截止到指定日期为止的季度集合
 * @param count 返回几个季度
 */
fun Date.getQuarterForDate(count: Int = 6): List<Date> {
    return (1..count).map {
        this.toDateTime().minusMonths((it - 1) * 3).toDate()
    }.reversed()
}