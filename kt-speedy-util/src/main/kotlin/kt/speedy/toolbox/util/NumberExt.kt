package kt.speedy.toolbox.util

import cn.hutool.core.util.NumberUtil
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

/**
 * 判断值是否在最小值和最大值内（包含两端）
 */
fun Int.isBetween(min: Int, max: Int): Boolean {
    return (min..max).contains(this)
}

/**
 * 转换成百分比的字符串形式，如24.8%。为空则默认显示 “-”
 */
fun Number?.toPercentStr(nullableChar: String = "-"): String {
    return if (this != null) {
        val nf = NumberFormat.getPercentInstance()
        nf.minimumFractionDigits = 1
        return nf.format(this)
    } else {
        nullableChar
    }
}

/**
 * 数字转换成字符串，为空则显示默认 nullableChar
 * @param nullableChar 为空显示的字符串，默认“-”
 */
fun Number?.toNumberStr(nullableChar: String = "-"): String {
    return this?.toString() ?: nullableChar
}

/**
 * 数字格式化为全是指定digit的值，比如 100.formatToSameDigit(8) = 888
 */
fun Int.formatToSameDigit(digit: Int): Int {
    return digit.toString().repeat(this.toString().length).toInt()
}

/**
 * 除
 */
fun Number.ddiv(value: Number, scale: Int = 3, errorReturnValue: Double = 0.0): Double {
    if (value.toDouble() == 0.0) {
        return errorReturnValue
    }
    return NumberUtil.div(this, value, scale).toDouble()
}

/**
 * 乘
 */
fun Number.mmul(value: Number): Number {
    return NumberUtil.mul(this, value)
}

/**
 * 加
 */
fun Number.aadd(value: Number): Number {
    return NumberUtil.add(this, value)
}

/**
 * 减
 */
fun Number.ssub(value: Number): Number {
    return NumberUtil.sub(this, value)
}

/**
 * 保留精度
 * @param scale 小数位
 * @param roundingMode 进位模式
 */
fun Number.round(scale: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): Double {
    return NumberUtil.round(this.toDouble(), scale, roundingMode).toDouble()
}

/**
 * 转换成百分比，默认保留3位小数
 * @param scale 保留小数位
 */
fun Number.toPercentNumber(scale: Int = 3): Double {
    return this.mmul(100).round(scale)
}

/**
 * 是否小于0，小于0则执行
 */
inline fun Number.ifLessThanZero(block: () -> Number): Number {
    return if (this.toDouble() < 0.0) {
        return block()
    } else {
        this
    }
}

/**
 * 元 转 分
 */
fun Double.convertYunToCent(): Int {
    val toDouble = BigDecimal(this).multiply(BigDecimal(100.0)).toDouble()
    return BigDecimal(toDouble).divide(BigDecimal(1), 0, BigDecimal.ROUND_HALF_UP).toDouble().toInt()
}

/**
 * 分 转 元
 */
fun Int.convertCentToYun(): Double {
    return this.ddiv(100, 2)
}

fun Int.toChinese(): String {
    if (this == 0) return "零"

    val units = listOf("", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿")
    val digits = listOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")

    var number = this
    var result = StringBuilder()
    var unitPos = 0
    var zero = true

    while (number > 0) {
        val digit = number % 10
        if (digit != 0) {
            result.insert(0, digits[digit] + units[unitPos])
            zero = false
        } else if (!zero) {
            result.insert(0, "零")
            zero = true
        }
        unitPos++
        number /= 10
    }

    // 处理特殊情况，如 "一十" 而不是 "一十零"
    if (result.startsWith("一十")) {
        result = StringBuilder(result.substring(1))
    }

    return result.toString()
}