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
 * 将指标名称替换成指定字符
 */
fun String.replaceIndicatorName(): String {
    return this.replace(".", "-")
        .replace("2", "")
}

fun String.findLine(startWith: String, removeStartWith: Boolean = false): String? {
    return this.split("\n").find { it.startsWith(startWith) }?.let {
        if (removeStartWith) {
            it.replaceFirst(startWith, "")
        } else {
            it
        }
    }
}


fun String.findLineAndRemoveChats(
    startWith: String,
    removeStartWith: Boolean = true,
    removeChars: List<String> = listOf(":", "：")
): String? {
    var str = this.findLine(startWith, removeStartWith)
    str?.let {
        removeChars.forEach {
            str = str?.replace(it, "")
        }
    }
    return str?.trim()
}

fun String.findByRegex(vararg regex: Regex): List<String> {
    regex.forEach { rex ->
        val result = rex.findAll(this)
            .map { it.groupValues.first() }
            .toList()
        if (result.isNotEmpty()) return result
    }
    return listOf()
}

fun String.replaceText(range: IntRange, replaceChar: String): String {
    if (range.first < 0 || range.last >= this.length) {
        throw IllegalArgumentException("Range out of bounds")
    }
    return this.replaceRange(range, replaceChar.repeat(range.count()))
}

fun String.findDateStrByRegex(): List<String> {
    val regex =
        Regex("""(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}[:：]\d{1,2}([:：]\d{1,2})?)|(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}([点时])\d{1,2}分(\d{1,2}秒)?)|(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?)""")
    return this.findByRegex(regex).map { it.trim() }
}

fun String.findMobileByRegex(): List<String> {
    val regex = Regex("""1[3-9]\d.{4}\d{4}""")
    return this.findByRegex(regex).map { it.trim() }
}

fun String.findDateStrByRegexAndPrefix(prefix: String): List<String> {
    val regexStr =
        """${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}[:：]\d{1,2}([:：]\d{1,2})?)|${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?\s*\d{1,2}([点时])\d{1,2}分(\d{1,2}秒)?)|${prefix}\s*(\d{4}[年-]\d{1,2}[月-]\d{1,2}[日\sT]?)"""
    val regex = regexStr.toRegex()
    return this.findByRegex(regex).map { it.replace(prefix, "").trim() }
}

/**
 * 冒号分割字符串
 */
fun String.splintLineByColon(vararg str: String): String {
    // 构建一个正则表达式，匹配任意一个分隔符
    val regex = str.joinToString("|") { Regex.escape(it) }.toRegex()

    // 根据正则表达式分割，并保留分隔符
    val splitParts = this.split(regex).filter { it.isNotEmpty() }
    val delimiters = regex.findAll(this).map { it.value }.toList()

    // 拼接结果时，重新插入分割词及其分隔符
    val result = StringBuilder()
    for (i in splitParts.indices) {
        if (i < delimiters.size) {
            result.append(delimiters[i])
        }
        result.append(splitParts[i])
        if (i < splitParts.size - 1) {
            result.append("\n")
        }
    }

    return result.toString()
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
fun String?.presentOrNull(): String? {
    return if (this.isNullOrBlank())
        null
    else
        this
}

fun String?.isPresent(): Boolean {
    return !this.isNullOrBlank()
}

/**
 * @param doIfPresent 值存在执行的动作
 * @param doIfNotPresent 值不存在执行的动作
 */
fun String?.ifPresent(doIfPresent: (a: String) -> Unit, doIfNotPresent: () -> Unit) {
    if (this.isNotPresent()) {
        doIfNotPresent()
    } else {
        doIfPresent(this!!)
    }
}

fun String?.ifPresent(doIfPresent: (a: String) -> Unit) {
    if (this.isPresent()) {
        doIfPresent(this!!)
    }
}

fun String?.isNotPresent(): Boolean {
    return !this.isPresent()
}

fun String.md5(): String {
    return EncryptExt.md5(this)
}

fun String.sh1(): String {
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

fun String.toDate(pattern: String): Date {
    return DateTimeExt.parse(this, pattern).toDate()
}

fun String.toDate(): Date {
    val formats = listOf(
        "yyyyMMdd",
        "yyyy-MM-dd",
        "yyyy-M-d",
        "yyyy/MM/dd",
        "yyyy/M/d",
        "yyyy年MM月dd日",
        "yyyy年M月d日",
        // 带时分秒
        "yyyyMMdd HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-M-d HH:mm:ss",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/M/d HH:mm:ss",
        "yyyy年MM月dd日 HH:mm:ss",
        "yyyy年MM月dd日HH:mm:ss",
        "yyyy年M月d日 HH:mm:ss",
        "yyyy年M月d日HH:mm:ss",

        "yyyyMMdd H:m:s",
        "yyyy-MM-dd H:m:s",
        "yyyy-M-d H:m:s",
        "yyyy/MM/dd H:m:s",
        "yyyy/M/d H:m:s",
        "yyyy年MM月dd日 H:m:s",
        "yyyy年MM月dd日H:m:s",
        "yyyy年M月d日 H:m:s",
        "yyyy年M月d日H:m:s",

        // 带时分
        "yyyyMMdd HH:mm",
        "yyyy-MM-dd HH:mm",
        "yyyy-M-d HH:mm",
        "yyyy/MM/dd HH:mm",
        "yyyy/M/d HH:mm",
        "yyyy年MM月dd日 HH:mm",
        "yyyy年MM月dd日HH:mm",
        "yyyy年M月d日 HH:mm",
        "yyyy年M月d日HH:mm",
        // 带时分
        "yyyyMMdd H:m",
        "yyyy-MM-dd H:m",
        "yyyy-M-d H:m",
        "yyyy/MM/dd H:m",
        "yyyy/M/d H:m",
        "yyyy年MM月dd日 H:m",
        "yyyy年MM月dd日H:m",
        "yyyy年M月d日 H:m",
        "yyyy年M月d日H:m",
    )
    // 预处理不规范的情况

    val text = this
        .trim()
        .replace("  ", " ")
        .replace(": ", ":")
        .replace(" :", ":")
        .replace(" : ", ":")

    formats.forEach { format ->
        try {
            return text.toDate(format)
        } catch (e: Exception) {
            // 继续处理
            println("ERROR: Exception while parsing date: $this, format: $format")
        }
    }
    throw Exception("未知格式的日期值：${this}")
}

fun String.toDateTime(pattern: String = "yyyy-MM-dd"): DateTime {
    return DateTimeExt.parse(this, pattern)
}

fun String.toDateTime(): DateTime {
    return this.toDate().toDateTime()
}

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

fun String.replaceLast(oldValue: String, newValue: String): String {
    return if (this.last().toString() == oldValue) {
        this.substring(0, this.length - 1) + newValue
    } else {
        this
    }
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

class StringExt {
    companion object {
        /**
         * 生成指定长度的随机字符串
         */
        fun randomStr(len: Int): String {
            var i: Int  // 生成的随机数
            var count = 0 // 生成的密码的长度
            // 密码字典
            val str = charArrayOf(
                'A',
                'B',
                'C',
                'D',
                'E',
                'F',
                'G',
                'H',
                'I',
                'J',
                'K',
                'L',
                'M',
                'N',
                'O',
                'P',
                'Q',
                'R',
                'S',
                'T',
                'U',
                'V',
                'W',
                'X',
                'Y',
                'Z',
                'a',
                'b',
                'c',
                'd',
                'e',
                'f',
                'g',
                'h',
                'i',
                'j',
                'k',
                'l',
                'm',
                'n',
                'o',
                'p',
                'q',
                'r',
                's',
                't',
                'u',
                'v',
                'w',
                'x',
                'y',
                'z',
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9'
            )
            val stringBuffer = StringBuffer("")
            val r = Random()
            while (count < len) {
                i = r.nextInt(str.size)
                stringBuffer.append(str[i])
                count++
            }
            return stringBuffer.toString()
        }
    }
}


