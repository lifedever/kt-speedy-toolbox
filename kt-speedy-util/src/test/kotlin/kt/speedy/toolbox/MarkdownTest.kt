package kt.speedy.toolbox

import kt.speedy.toolbox.util.MarkdownTableUtil
import org.junit.jupiter.api.Test

class MarkdownTest {

    @Test
    fun test1() {
        val markdownTable = """
| 姓名 | 年龄 | 职业 |
|------|------|------|
| 张三 | 25   | 工程师 |
| 李四 | 30   | 设计师 |
""".trimIndent()

        MarkdownTableUtil.toJsonArray(markdownTable).forEach { println(it) }
    }
}