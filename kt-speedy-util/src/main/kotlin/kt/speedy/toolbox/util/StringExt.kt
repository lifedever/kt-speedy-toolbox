package kt.speedy.toolbox.util

import cn.hutool.http.HtmlUtil
import org.joda.time.DateTime
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 将字符路径中的/替换，防止出现下载意外
 */
fun String.trimPath(): String {
    val name = this.replace("/", "-").replace(" ", "-")
    val regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]"
    val p: Pattern = Pattern.compile(regEx)
    val m: Matcher = p.matcher(name)
    return m.replaceAll("-")
}

/**
 * 防止特殊符号导致mongodb查询出错
 */
fun String.escapeSpecialWordForMongoQuery(): String {
    var keyword = this
    val fbsArr = arrayOf("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|")
    for (key in fbsArr) {
        if (keyword.contains(key)) {
            keyword = keyword.replace(key, "\\" + key)
        }
    }
    return keyword
}

/**
 * 还原被转义的HTML特殊字符
 */
fun String.htmlUnescape(): String {
    return HtmlUtil.unescape(this)
}

/**
 * 去除HTML标记
 */
fun String.clearHtmlTag(): String {
    return HtmlUtil.cleanHtmlTag(this)
}

/**
 * 根据 startWith 获取某一行
 * @param startWith 查找的字符串
 * @param removeStartWith 是否删除查找的字符串
 *
 * “东方森东方
 * 经核查：xxxxx”
 * 发动发动发动发动
 * “东发动发动”
 */
fun String.findLine(startWith: String, removeStartWith: Boolean = false): String? {
    return this.split("\n").find { it.contains(startWith) }?.let {
        if (removeStartWith) {
            it.replaceBefore(startWith, "").replaceFirst(startWith, "")
        } else {
            it
        }
    }
}

/**
 * 根据 startWith 获取某一行，并且删除特殊字符，默认为：":", "："
 */
fun String.findLineAndRemoveChats(
    startWith: String,
    removeStartWith: Boolean = true,
    removeChars: List<String> = listOf(":", "："),
): String? {
    var str = this.findLine(startWith, removeStartWith)
    str?.let {
        removeChars.forEach {
            str = str?.replace(it, "")
        }
    }
    return str?.trim()
}

/**
 * 根据正则返回查找的字符串集合
 */
fun String.findByRegex(vararg regex: Regex): List<String> {
    regex.forEach { rex ->
        val result = rex.findAll(this)
            .map { it.groupValues.first() }
            .toList()
        if (result.isNotEmpty()) return result
    }
    return listOf()
}

/**
 * 替换字符串
 */
fun String.replaceText(range: IntRange, replaceChar: String): String {
    if (range.first < 0 || range.last >= this.length) {
        throw IllegalArgumentException("Range out of bounds")
    }
    return this.replaceRange(range, replaceChar.repeat(range.count()))
}

/**
 * 返回字符串中匹配的日期集合
 */
fun String.findDateStrByRegex(): List<String> {
    val regex =
        Regex("""(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}[:：]\d{1,2}([:：]\d{1,2})?)|(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}([点时])\d{1,2}分(\d{1,2}秒)?)|(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?)|(\d{4}[年-]\d{1,2}月?)|(\d{4}年?)|(\d{1,2}[月-]\d{1,2}[日\sT])|(\d{1,2}月)|(\d{1,2}日)""")
    return this.findByRegex(regex).map { it.trim().replace("：", ":") }
}

/**
 * 返回字符串中匹配的手机号集合
 */
fun String.findMobileByRegex(): List<String> {
    val regex = Regex("""1[3-9]\d.{4}\d{4}""")
    return this.findByRegex(regex).map { it.trim() }
}

/**
 * 根据前缀查找返回字符串中涉及的日期
 */
fun String.findDateStrByRegexAndReplacePrefix(prefix: String): List<String> {
    val regexStr =
        """${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}[:：]\d{1,2}([:：]\d{1,2})?)|${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}([点时])\d{1,2}分(\d{1,2}秒)?)|${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?)"""
    val regex = regexStr.toRegex()
    return this.findByRegex(regex).map { it.replace(prefix, "").trim() }
}

/**
 * 根据指定的字符串进行按行分割
 * 情况1：有，或者无
 * 情况2：有，或者无
 * 情况3：有，或者无
 *
 * 情况1：有情况2：有
 * 情况3：有
 */
fun String.splintLineByColon(vararg delimiters: String): String {
    val text = this.replace("\n", "")
    // 找到每个分割符在输入字符串中的位置，并按顺序排序
    val sortedDelimiters = delimiters.mapNotNull { delimiter ->
        val index = text.indexOf(delimiter)
        if (index != -1) delimiter to index else null
    }.sortedBy { it.second }.map { it.first }

    var remainingText = text
    val result = StringBuilder()

    for (delimiter in sortedDelimiters) {
        val index = remainingText.indexOf(delimiter)
        if (index != -1) {
            // 追加分割符及其后内容
            result.append(remainingText.substring(index, index + delimiter.length))

            // 获取分割符后面的内容，直到下一个分割符或结尾
            val rest = remainingText.substring(index + delimiter.length)
            val nextIndex = sortedDelimiters.drop(sortedDelimiters.indexOf(delimiter) + 1)
                .map { rest.indexOf(it) }
                .filter { it != -1 }
                .minOrNull() ?: rest.length

            result.append(rest.substring(0, nextIndex)).append("\n")

            // 更新remainingText为剩余部分
            remainingText = rest.substring(nextIndex)
        }
    }

    return result.toString().trim()
}

/**
 * 路径追加
 */
fun String.pathAppend(path: String): String {
    return this.removeSuffix("/") + "/" + path.removeSuffix("/").removePrefix("/")
}

/**
 * 获取文件类型
 */
fun String.getFileType(): String {
    return this.substringAfterLast(".").lowercase()
}

/**
 * like 查询拼接
 */
fun String.toLikeQuery(): String {
    return "%$this%"
}

/**
 * like 左侧模糊匹配
 */
fun String.toLeftLikeQuery() = "%$this"

/**
 * like 右侧模糊匹配
 */
fun String.toRightLikeQuery() = "$this%"

/**
 * 最大字符长度
 */
fun String.substringMax(count: Int): String {
    return if (this.length <= count)
        this
    else {
        this.substring(0, count)
    }
}

/**
 * 字符串不为空或空白
 */
fun String?.nullIfBlank(): String? {
    return if (this.isNullOrBlank())
        null
    else
        this
}

/**
 * 判断字符串不为空或者不为空字符
 */
fun String?.isPresent(): Boolean {
    return !this.isNullOrBlank()
}

/**
 * @param doIfPresent 值存在执行的动作
 * @param doIfNotPresent 值不存在执行的动作
 */
inline fun String?.ifPresent(doIfPresent: (a: String) -> Unit, doIfNotPresent: () -> Unit) {
    if (this.isNotPresent()) {
        doIfNotPresent()
    } else {
        doIfPresent(this!!)
    }
}

inline fun String?.ifPresent(doIfPresent: (a: String) -> Unit) {
    if (this.isPresent()) {
        doIfPresent(this!!)
    }
}

fun String?.ifPresentOrElse(other: String): String {
    return if (this.isNotPresent()) {
        other
    } else {
        this!!
    }
}

fun String?.isNotPresent(): Boolean {
    return !this.isPresent()
}

/**
 * 返回md5
 */
fun String.md5(): String {
    return EncryptExt.md5(this)
}

/**
 * 返回sha1
 */
fun String.sha1(): String {
    return EncryptExt.sha1(this)
}

/**
 * 去除字符串中间所有的空格\t、回车\n、换行符\r、制表符\t
 */
fun String.trimForce(): String {
    val p = Pattern.compile("\\s*|\t|\r|\n")
    val m = p.matcher(this)
    return m.replaceAll("")
}

/**
 * 去除unicode 字符
 */
fun String.trimChar(char: Int): String {
    return this.replace(Char(char).toString(), "")
}

/**
 * 转换成日期
 */
fun String.toDate(pattern: String): Date {
    return DateTimeExt.parse(this, pattern).toDate()
}

/**
 * 自动转换成日期
 * 注意开发的时候，格式的顺序需要按照长短从长到短排列
 */
fun String.toDate(): Date {
    val formats = listOf(
        "yyyy年MM月dd日 HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-ddHH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy年MM月dd日HH:mm:ss",
        "yyyy年M月d日 HH:mm:ss",
        "yyyyMMdd HH:mm:ss",
        "yyyy-M-d HH:mm:ss",
        "yyyy/M/d HH:mm:ss",
        "yyyy年M月d日HH:mm:ss",
        "yyyy年MM月dd日 H:m:s",
        "yyyy年MM月dd日 HH:mm",
        "yyyy-MM-dd H:m:s",
        "yyyy/MM/dd H:m:s",
        "yyyy年MM月dd日H:m:s",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-ddHH:mm",
        "yyyy/MM/dd HH:mm",
        "yyyy年MM月dd日HH:mm",
        "yyyy年M月d日 H:m:s",
        "yyyy年M月d日 HH:mm",
        "yyyy年MM月dd日 H:m",
        "yyyyMMdd H:m:s",
        "yyyy-M-d H:m:s",
        "yyyy/M/d H:m:s",
        "yyyy年M月d日H:m:s",
        "yyyyMMdd HH:mm",
        "yyyy-M-d HH:mm",
        "yyyy/M/d HH:mm",
        "yyyy年M月d日HH:mm",
        "yyyy-MM-dd H:m",
        "yyyy/MM/dd H:m",
        "yyyy年MM月dd日H:m",
        "yyyyMMddHHmmss",
        "yyyy年M月d日 H:m",
        "yyyyMMdd H:m",
        "yyyy-M-d H:m",
        "yyyy/M/d H:m",
        "yyyy年M月d日H:m",
        "yyyyMMddHHmm",
        "yyyy年MM月dd日",
        "yyyy-MM-dd",
        "yyyy/MM/dd",
        "yyyyMMddHH",
        "yyyy年M月d日",
        "yyyy年MM月",
        "yyyy-M-d",
        "yyyy/M/d",
        "yyyyMMdd",
        "yyyy-MM",
        "yyyy-MM月",
        "yyyy/MM",
        "yyyy年M月",
        "yyyy-M",
        "yyyy-M月",
        "yyyy/M",
        "MM月dd日",
        "yyyy年",
        "MM-dd",
        "MM/dd",
        "yyyy",
        "MMdd",
        "M月d日",
        "M-d",
        "M/d",
        "MM月",
        "dd日",
        "Md",
        "M月",
        "d日",
    )
    // 预处理不规范的情况

    val text = this
        .trim()
        .replace("  ", " ")
        .replace(": ", ":")
        .replace(" :", ":")
        .replace(" : ", ":")
        .replace("时", ":")
        .replace("分", ":")
        .replace("秒", "")
    formats.forEach { format ->
        try {
            return text.toDate(format)
        } catch (e: Exception) {
            // 继续处理
            println("ERROR: Exception while parsing date: $this, format: $format")
        }
    }
    throw IllegalArgumentException("未知格式的日期值：${this}")
}

/**
 * 自动转换日期，如果转换失败则返回null
 */
fun String.toDateOrNull(): Date? {
    return try {
        this.toDate()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String.toDateTime(pattern: String = "yyyy-MM-dd"): DateTime {
    return DateTimeExt.parse(this, pattern)
}

fun String.toDateTime(): DateTime {
    return this.toDate().toDateTime()
}

/**
 * 返回字符串分割后的数组的第index个元素，如果没有返回null
 */
fun String.splitChild(delimiter: String, index: Int): String? {
    val sts = this.split(delimiter)
    return if (sts.size > index) {
        sts[index]
    } else {
        null
    }
}

/**
 * 判断指定的 {text} 是否包含 words 中的任意单词
 */
fun String.containsAnyWord(words: Collection<String>): List<String> {
    if (words.any { it == "*" })
        return listOf("*")
    return words.filter { this.contains(it) }.toList()
}

/**
 * 替换最后一个匹配项
 */
fun String.replaceLast(oldValue: String, newValue: String): String {
    if (!this.endsWith(oldValue)) {
        return this // 如果不是以 oldValue 结尾，直接返回原字符串
    }
    val lastIndex = this.lastIndexOf(oldValue)
    return this.substring(0, lastIndex) + newValue
}


/**
 * 批量将多个词替换成 1 个词
 */
fun String.replaceBatch(oldValues: List<String>, newValue: String): String {
    var str = this
    oldValues.forEach {
        str = str.replace(it, newValue)
    }
    return str
}

/**
 * 将中文数字转换成阿拉伯数字
 */
fun String.toArabicNumber(): Int {
    val chineseNumbers = mapOf(
        "一" to 1, "二" to 2, "三" to 3, "四" to 4, "五" to 5,
        "六" to 6, "七" to 7, "八" to 8, "九" to 9, "十" to 10,
        "百" to 100
    )

    var result = 0
    var temp = 0
    var lastUnit = 1

    for (char in this) {
        val num = chineseNumbers[char.toString()]
        if (num != null) {
            if (num == 10 || num == 100) {
                if (temp == 0) temp = 1
                result += temp * num
                temp = 0
                lastUnit = num
            } else {
                temp = num
            }
        }
    }

    result += temp
    return result
}

fun String.replaceCnNumToInt(): String {
    return this.replaceCnNumToInt("")
}

fun String.replaceCnNumToInt(suffix: String): String {
    var newText = this
    val map = mapOf(
        "一" to 1,
        "二" to 2,
        "三" to 3,
        "四" to 4,
        "五" to 5,
        "六" to 6,
        "七" to 7,
        "八" to 8,
        "九" to 9,
        "十" to 10,
        "十二" to 12,
        "二十四" to 24,
        "三十六" to 36,
    )
    map.keys.forEach { key ->
        newText = newText.replace(key + suffix, map[key].toString() + suffix)
    }
    return newText
}

/**
 * 如果等于某个值，则执行
 */
inline fun String.ifEquals(other: String, block: (String) -> Unit) {
    if (this == other) {
        block(this)
    }
}

/**
 * 如果包含某个值，则执行
 */
inline fun String.ifContains(other: String, block: () -> Unit) {
    if (this.contains(other)) {
        block()
    }
}

/**
 * 如果字符串不存在或者等于某个值，则执行block
 */
inline fun String?.ifNotPresentOrEqual(equalStr: String, block: () -> String): String {
    return if (this.isNotPresent() || this == equalStr) block() else this!!
}

/**
 * 如果字符串不存在或者等于某个值，则执行block
 */
fun String?.ifNotPresentOrEqual(equalStr: String, replaceChar: String): String {
    return if (this.isNotPresent() || this == equalStr) replaceChar else this!!
}

/**
 * 如果字符串不存在或者包含某个值，则执行block
 */
inline fun String?.ifNotPresentOrContains(str: String, block: () -> String): String {
    return if (this.isNotPresent() || this!!.contains(str)) block() else this
}

/**
 * 如果字符串不存在或者包含某个值，则执行block
 */
fun String?.ifNotPresentOrContains(str: String, replaceChar: String): String {
    return if (this.isNotPresent() || this!!.contains(str)) replaceChar else this
}

/**
 * 是否乱码
 */
fun String.isMessyCode(): Boolean {
    for (element in this) {
        // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
        // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
        if (element.code == 0xfffd) {
            // 存在乱码
            return true
        }
    }
    return false
}

/**
 * 获取指定头部和尾部变量之间截取内容（包含头尾）的功能
 */
fun String.findBetween(start: String, end: String, containsStartAndEnd: Boolean = true): String? {
    val startIndex = this.indexOf(start)
    val endIndex = this.indexOf(end, startIndex + start.length)

    val subStr = if (startIndex != -1 && endIndex != -1) {
        this.substring(startIndex, endIndex + end.length)
    } else {
        null
    }

    return if (containsStartAndEnd) {
        subStr
    } else {
        subStr?.replaceFirst(start, "")?.replaceLast(end, "")
    }
}

/**
 * 根据真实的字符长度分割字符串，中文占 2，英文占 1
 * @param size 分割长度
 */
fun String.chunkedRealSizeByCharset(size: Int): List<String> {
    var sb = StringBuilder()
    var length = 0
    val result = mutableListOf<String>()
    this.forEach { char ->
        val charSize = char.toString().toByteArray(charset = Charsets.UTF_8).size.let {
            if (it == 3) {
                2
            } else {
                1
            }
        }
        if (length + charSize > size) {
            result.add(sb.toString())
            sb = StringBuilder()
            length = 0
        }
        sb.append(char)
        length += charSize
    }

    if (sb.isNotEmpty()) {
        result.add(sb.toString())
    }
    return result
}

/**
 * 删除末尾指定字符
 */
fun String.removeLast(char: String): String {
    return if (this.endsWith(char)) {
        this.dropLast(char.length)
    } else {
        this
    }
}

/**
 * 首字母变成小写
 */
fun String.lowerFirstChat(): String {
    return this.replaceFirstChar { it.lowercaseChar() }
}

/**
 * 首字母变成大写
 */
fun String.upperFirstChat(): String {
    return this.replaceFirstChar { it.uppercase() }
}

/**
 * 解析true或false
 */
fun String.getBooleanByRegex(regexStr: String): Boolean {
    return regexStr.toRegex(RegexOption.DOT_MATCHES_ALL)
        .find(this)
        ?.groupValues?.get(1)
        ?.trim() // 去除首尾空格
        ?.toBooleanStrict() == true
}

/**
 * 是否包含指定字符串的任意一个
 */
fun String.containsAnyOf(vararg keywords: String, ignoreCase: Boolean = false): Boolean {
    return keywords.any { this.contains(it, ignoreCase) }
}

/**
 * 是否为图像文件
 */
fun String.isImage(): Boolean {
    val imageExtensions = listOf(
        ".jpg", ".jpeg", ".png",
        ".gif", ".bmp", ".webp",
        ".svg", ".tiff", ".ico"
    )

    // 移除 URL 查询参数和片段标识符
    val cleanPath = this
        .substringBefore('?')
        .substringBefore('#')
        .trim()
    return imageExtensions.any {
        cleanPath.endsWith(it, ignoreCase = true)
    }
}

/**
 * 处理文件名
 */
fun String.sanitizeFileName(
    replacement: String = "_",
    removeConsecutiveReplacement: Boolean = true,
    trim: Boolean = true,
): String {
    // 定义不允许在文件名中使用的字符
    val invalidChars = Regex("""[\\/:*?"<>|\s]""")
    var result = this.replace(invalidChars, replacement)

    // 处理连续替换字符（如多个空格变成多个_）
    if (removeConsecutiveReplacement && replacement.isNotEmpty()) {
        val consecutiveReplacements = Regex(Regex.escape(replacement) + "+")
        result = result.replace(consecutiveReplacements, replacement)
    }

    // 去除首尾空白和替换字符
    if (trim) {
        result = result.trim().trim { it.toString() == replacement }
    }

    return result
}