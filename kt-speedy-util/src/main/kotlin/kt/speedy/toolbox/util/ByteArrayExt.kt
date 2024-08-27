package kt.speedy.toolbox.util

/**
 * ByteArray 转换成 16进制
 */
fun ByteArray.toHex() : String{
    val hexChars = "0123456789ABCDEF".toCharArray()
    val result = StringBuffer()
    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(hexChars[firstIndex])
        result.append(hexChars[secondIndex])
    }

    return result.toString()
}