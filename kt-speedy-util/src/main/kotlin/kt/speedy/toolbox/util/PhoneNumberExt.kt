package kt.speedy.toolbox.util

import cn.hutool.core.util.RandomUtil
import kotlin.random.Random

object PhoneNumberExt {
    /**
     * 获取中国移动号码
     */
    fun getPhoneNumberSegment(): Array<String> {
        return arrayOf(
            "1340", "1341", "1342", "1343", "1344", "1345", "1346", "1347", "1348",
            "135", "136", "137", "138", "139",
            "147",
            "150", "151", "152", "157", "158", "159",
            "172", "178",
            "182", "183", "184", "187", "188",
            "195", "197", "198",
        )
    }

    /**
     * 随机生成移动电话号码
     */
    fun getRandomPhoneNumber(): String {
        val telFirsts = this.getPhoneNumberSegment()
        val prefix = telFirsts[Random.nextInt(0, telFirsts.size - 1)]
        return (prefix + RandomUtil.randomInt(10000000, 99999999).toString()).substring(0, 11)
    }

    /**
     * 通过正则提取手机号
     */
    fun extractPhoneNumbers(text: String): List<String> {
        val regex = Regex("""1[\d*]{2}[\d*]{4}[\d*]{4}""")
        // 查找所有匹配的手机号
        val matches = regex.findAll(text).map { it.value }.toList()
        return matches
    }
}