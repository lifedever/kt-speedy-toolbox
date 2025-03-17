package kt.speedy.toolbox.util

import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil

/**
 * Markdown表格转JSONObject集合工具类
 */
object MarkdownTableUtil {
    
    /**
     * 将Markdown表格字符串转换为JSONObject集合
     * @param markdownTable Markdown表格字符串
     * @return JSONObject集合
     */
    fun toJsonArray(markdownTable: String): List<JSONObject> {
        val lines = markdownTable.trim().split("\n")
        if (lines.size < 3) {
            return emptyList()
        }

        // 解析表头
        val headers = lines[0].split("|")
            .filter { it.isNotBlank() }
            .map { it.trim() }

        // 验证分隔行
        val separatorLine = lines[1]
        if (!separatorLine.matches(Regex("\\|\\s*[-:]+[-|\\s:]*\\|"))) {
            throw IllegalArgumentException("Invalid markdown table separator line")
        }

        // 解析数据行
        return lines.drop(2).map { line ->
            val values = line.split("|")
                .filter { it.isNotBlank() }
                .map { it.trim() }

            if (values.size != headers.size) {
                throw IllegalArgumentException("Row data count does not match header count")
            }

            JSONUtil.createObj().apply {
                headers.forEachIndexed { index, header ->
                    put(header, values[index])
                }
            }
        }
    }

    /**
     * 将JSONObject集合转换为Markdown表格字符串
     * @param jsonArray JSONObject集合
     * @return Markdown表格字符串
     */
    fun toMarkdownTable(jsonArray: List<JSONObject>): String {
        if (jsonArray.isEmpty()) {
            return ""
        }

        val headers = jsonArray[0].keys.toList()
        val headerRow = "| ${headers.joinToString(" | ")} |"
        val separatorRow = "| ${headers.joinToString(" | ") { "---" }} |"
        val dataRows = jsonArray.map { json ->
            "| ${headers.joinToString(" | ") { header -> json.getStr(header) }} |"
        }

        return (listOf(headerRow, separatorRow) + dataRows).joinToString("\n")
    }
} 