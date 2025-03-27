package kt.speedy.toolbox.util

/**
 * 如果为True，则执行
 */
inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

/**
 * 如果为False，则执行
 */
inline fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) {
        block()
    }
}