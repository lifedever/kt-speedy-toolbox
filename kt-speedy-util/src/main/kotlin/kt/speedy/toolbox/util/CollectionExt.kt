package kt.speedy.toolbox.util

import cn.hutool.core.bean.BeanUtil

/**
 * 集合是否不为空
 */
fun <T> Collection<T>?.isPresent(): Boolean {
    return !this.isNullOrEmpty()
}

/**
 * 如果集合不为空，则执行
 */
fun <T> Collection<T>?.ifPresent(block: () -> Unit) {
    if (this.isPresent()) block()
}

/**
 * 集合是否为空
 */
fun <T> Collection<T>?.isNotPresent(): Boolean {
    return !this.isPresent()
}

/**
 * 如果集合为空，则执行
 */
fun <T> Collection<T>?.ifNotPresent(block: () -> Unit) {
    if (this.isNotPresent()) block()
}

/**
 * 将递归集合转换成平级的
 */
@Suppress("UNCHECKED_CAST")
fun <T> Collection<T>.toParallels(childrenKey: String = "children"): Collection<T> {
    val list = mutableListOf<T>()
    this.forEach { item ->
        list.add(item)
        val children = BeanUtil.getFieldValue(item, childrenKey)
        if (children != null && children is Collection<*>) {
            list.addAll(children.toParallels(childrenKey) as Collection<T>)
        }
    }

    return list
}