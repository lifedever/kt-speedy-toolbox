package kt.speedy.toolbox.util

import cn.hutool.core.date.DateUtil
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
            return DateTime
                .now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))
                .toDate()
        }
    }
}

/**
 * 指定某一天
 * @param day 如：01、15
 */
fun DateTime.dayOfThisMonth(day: String): DateTime {
    return this.toString("yyyy-MM-${day}").toDateTime()
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


/**
 * 获取一天的最开始
 */
fun DateTime.startOfDay(): DateTime {
    return this.millisOfDay().withMinimumValue()
}

/**
 * 获取一天的结束
 */
fun DateTime.endOfDay(): DateTime {
    return this.millisOfDay().withMaximumValue()
}

/**
 * 获取一周的开始
 */
fun DateTime.startOfWeek(): DateTime {
    return this.dayOfWeek().withMinimumValue().millisOfDay().withMinimumValue()
}

/**
 * 获取一周的结束
 */
fun DateTime.endOfWeek(): DateTime {
    return this.dayOfWeek().withMaximumValue().millisOfDay().withMaximumValue()
}

/**
 * 获取一月的开始
 */
fun DateTime.startOfMonth(): DateTime {
    return this.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue()
}

/**
 * 获取一月的结束
 */
fun DateTime.endOfMonth(): DateTime {
    return this.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue()
}
/**
 * 获取一年的开始
 */
fun DateTime.startOfYear(): DateTime {
    return this.dayOfYear().withMinimumValue().millisOfDay().withMinimumValue()
}

/**
 * 获取一年的结束
 */
fun DateTime.endOfYear(): DateTime {
    return this.dayOfYear().withMaximumValue().millisOfDay().withMaximumValue()
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
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
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

/**
 * 天数转日期
 */
fun Int.dayToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = DateUtil.parse("1900-01-01", "yyyy-MM-dd")
    calendar.add(Calendar.DAY_OF_YEAR, this - 2)
    return calendar.time
}

/**
 * 获取两个日期之间的所有月份（包含这两个日期）
 * @param end 结束日期
 * @return 月份列表（从小到大排序）
 */
fun DateTime.getMonthsBetween(end: DateTime): List<DateTime> {
    val months = mutableListOf<DateTime>()
    val startMonth = this.startOfMonth()
    val endMonth = end.startOfMonth()
    
    // 确定开始和结束月份
    val (first, last) = if (startMonth.isBefore(endMonth)) {
        startMonth to endMonth
    } else {
        endMonth to startMonth
    }
    
    var current = first
    while (!current.isAfter(last)) {
        months.add(current)
        current = current.plusMonths(1)
    }
    
    return months
}