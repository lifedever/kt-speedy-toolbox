package kt.speedy.toolbox.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 并行执行
 */
fun <T> parallelExec(
    context: CoroutineContext = Dispatchers.Default,
    vararg tasks: suspend () -> T,
    callback: (List<Result<T>>) -> Unit,
) {
    val scope = CoroutineScope(context + Job())
    scope.launch {
        try {
            val deferredResults = tasks.map { task ->
                async {
                    runCatching { task() }
                }
            }
            val results = deferredResults.awaitAll()
            callback(results)
        } finally {
            scope.coroutineContext.cancel()
        }
    }
}