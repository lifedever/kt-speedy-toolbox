package kt.speedy.toolbox.util

import cn.hutool.core.bean.BeanUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

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

/**
 * 并发执行多个任务
 */
suspend fun <T, R> Collection<T>.parallelRunTasks(limit: Int, await: Boolean = true, call: (t: T) -> R) {
    val items = this
    val channel = Channel<T>(Channel.UNLIMITED)
    coroutineScope {
        // 启动生产者协程
        launch {
            items.filterNotNull().forEach { ruler ->
                channel.send(ruler)
            }
            channel.close()
        }

        // 启动固定数量的消费者协程
        val jobs = List(limit) {
            launch(Dispatchers.Default) {
                for (t in channel) {
                    call(t)
                }
            }
        }
        // 等待所有任务完成
        if (await) {
            jobs.joinAll()
        }
    }
}