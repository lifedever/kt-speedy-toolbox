package kt.speedy.toolbox

import kt.speedy.toolbox.util.ifNotPresentOrEqual
import kotlin.test.Test

class StringTest {

    @Test
    fun test1() {
        val str = "无".ifNotPresentOrEqual("无") {
            "【员工补充：ssssss】"
        }
        println(str)
    }
}