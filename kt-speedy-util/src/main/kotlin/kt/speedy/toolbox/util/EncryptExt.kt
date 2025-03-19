package kt.speedy.toolbox.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncryptExt {

    // md5加密 (不推荐用于密码存储)
    fun md5(inputText: String): String {
        return encrypt(inputText, "md5")
    }

    // sha1加密 (不推荐用于密码存储)
    fun sha1(inputText: String): String {
        return encrypt(inputText, "sha-1")
    }

    // sha256加密
    fun sha256(inputText: String): String {
        return encrypt(inputText, "sha-256")
    }

    // sha512加密
    fun sha512(inputText: String): String {
        return encrypt(inputText, "sha-512")
    }

    /**
     * 哈希加密
     *
     * @param inputText 要加密的内容
     * @param algorithm 加密算法名称：md5、sha-1、sha-256、sha-512等
     * @return 加密后的字符串
     * @throws IllegalArgumentException 当输入为空或算法名称无效时
     * @throws NoSuchAlgorithmException 当指定的算法不存在时
     */
    private fun encrypt(inputText: String?, algorithm: String?): String {
        if (inputText.isNullOrBlank()) {
            throw IllegalArgumentException("请输入要加密的内容")
        }
        if (algorithm.isNullOrBlank()) {
            throw IllegalArgumentException("请指定加密算法")
        }

        try {
            val m = MessageDigest.getInstance(algorithm)
            m.update(inputText.toByteArray(StandardCharsets.UTF_8))
            return m.digest().toHex()
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("不支持的加密算法: $algorithm", e)
        }
    }

    /**
     * 生成带盐值的SHA-1摘要
     * 注意：此方法仅用于兼容旧系统，新系统建议使用更安全的算法
     */
    fun sha1MessageDigest(saltKey: String, text: String): String {
        return sha1("$text{$saltKey}")
    }
}
