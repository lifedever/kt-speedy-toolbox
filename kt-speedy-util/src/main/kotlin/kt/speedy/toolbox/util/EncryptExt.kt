package kt.speedy.toolbox.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object EncryptExt {

    // md5加密
    fun md5(inputText: String): String {
        return encrypt(inputText, "md5")
    }

    // sha加密
    fun sha1(inputText: String): String {
        return encrypt(inputText, "sha-1")
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText
     * 要加密的内容
     * @param algorithm
     * 加密算法名称：md5或者sha-1，不区分大小写
     * @return
     */
    private fun encrypt(inputText: String?, algorithm: String?): String {
        var algorithmName = algorithm
        if (inputText == null || "" == inputText.trim { it <= ' ' }) {
            throw IllegalArgumentException("请输入要加密的内容")
        }
        if (algorithmName == null || "" == algorithmName.trim { it <= ' ' }) {
            algorithmName = "md5"
        }
        val encryptText = ""
        try {
            val m = MessageDigest.getInstance(algorithmName)
            m.update(inputText.toByteArray(charset("UTF8")))
            val s = m.digest()
            // m.digest(inputText.getBytes("UTF8"));
            return s.toHex()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return encryptText
    }

    /**
     * 生成摘要
     */
    fun sha1MessageDigest(saltKey: String, text: String): String? {
        return sha1(String.format("%s{%s}", text, saltKey))
    }
}
