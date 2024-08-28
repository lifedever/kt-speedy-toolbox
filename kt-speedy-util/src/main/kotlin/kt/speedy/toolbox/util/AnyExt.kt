package kt.speedy.toolbox.util

inline fun Any?.ifNotNull(block: () -> Unit) {
    if (this != null) {
        block()
    }
}

inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

inline fun <T> Any.ifPresent(block: (a: T) -> Unit) {
    (this as? T)?.let {
        block(it)
    }
}

fun <T> required(any: T?, errorMsg: String): T {
    return any ?: throw IllegalArgumentException(errorMsg)
}

inline fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return !this.isNull()
}
