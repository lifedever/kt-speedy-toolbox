package kt.speedy.toolbox.util

import cn.hutool.core.util.NumberUtil
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

fun Int.isBetween(min: Int, max: Int): Boolean {
    return this in min..max
}

fun Number?.toPercentStr(): String {
    return if (this != null) {
        val nf = NumberFormat.getPercentInstance()
        nf.minimumFractionDigits = 1
        return nf.format(this)
    } else {
        "-"
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
 * 小于0
 */
fun Number.ifLessThenZero(doSomeThing: () -> Number): Number {
    return if (this.toDouble() < 0.0) {
        return doSomeThing()
    } else {
        this
    }
}

/**
 * 元转分
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