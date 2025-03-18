package kt.speedy.toolbox.data.jpa.util

import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

object TransactionUtil {
    fun triggerAfterCommit(call: () -> Unit) {
        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                // 事务提交后的操作
                // 附件
                call()
            }
        })
    }
}