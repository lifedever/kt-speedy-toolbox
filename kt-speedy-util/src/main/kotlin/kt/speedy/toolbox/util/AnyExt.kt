package kt.speedy.toolbox.util

import java.math.BigDecimal

/**
 * 判断是否为空
 */
fun Any?.isNull(): Boolean {
    return this == null
}

/**
 * 判断是否不为空
 */
fun Any?.isNotNull(): Boolean {
    return !this.isNull()
}

/**
 * 如果不为空，则执行
 */
inline fun Any?.ifNotNull(block: () -> Unit) {
    if (this != null) {
        block()
    }
}

/**
 * 如果为空，则执行
 */
inline fun Any?.ifNull(block: () -> Unit) {
    if (this != null) {
        block()
    }
}

/**
 * 如果存在，则执行
 */
inline fun <T> Any.ifPresent(block: (a: T) -> Unit) {
    (this as? T)?.let {
        block(it)
    }
}

/**
 * 返回当前值或者指定值
 */
fun <T> T?.assertRequired(errorMsg: String): T {
    return this ?: throw IllegalArgumentException(errorMsg)
}

/**
 * 为空则返回默认
 */
fun <T> T?.orElse(default: T): T {
    return this ?: default
}

/**
 * 两个值都为空则执行
 */
inline fun <T1, T2> ifBothNotNull(value1: T1?, value2: T2, block: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        block(value1, value2)
    }
}

/**
 * 安全转换成Int
 */
fun Any.toSafeInt(): Int? = when (this) {
    is BigDecimal -> this.intValueExact()
    is Int -> this
    else -> null
}

fun Any.toSafeInt(default: Int): Int = this.toSafeInt() ?: default

/**
 * 安全转换成Double
 */
fun Any?.toSafeDouble(): Double? = when (this) {
    is BigDecimal -> this.toDouble()
    is Int -> this.toDouble()
    is Long -> this.toDouble()
    is Double -> this
    else -> null
}
fun Any?.toSafeDouble(default: Double): Double = this.toSafeDouble() ?: default