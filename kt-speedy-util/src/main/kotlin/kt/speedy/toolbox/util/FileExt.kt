package kt.speedy.toolbox.util

import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

/**
 * 临时生成文件，用完后自动删除
 */
fun File.tempUse(block: () -> Unit) {
    block()
    this.delete()
}

/**
 * 判断是否为图片
 */
fun File.isImageFile(): Boolean {
    return this.name.isImage()
}

fun String.filterFiles(): Boolean {
    val filter = arrayOf(
        "__MACOSX",
        ".DS_Store",
    )

    return filter.none { it == this } && !this.startsWith(".")
}

/**
 * 获取文件类型
 */
fun File.getFileType(): String {
    return this.name.substringAfterLast('.').lowercase()
}

/**
 * 将字符串路径转换为文件对象
 */
fun String.toFile(): File {
    return File(this)
}

/**
 * 图片截取
 * @param xRate x 坐标的百分比位置
 * @param yRate y 坐标的百分比位置
 * @param wRate 宽度的百分比
 * @param hRate 高度得百分比
 */
fun File.imageCut(xRate: Double, yRate: Double, wRate: Double, hRate: Double): ByteArray {
    val out = ByteArrayOutputStream()
    val bufferedImage = ImageIO.read(this)

    val imageWidth = bufferedImage.width
    val imageHeight = bufferedImage.height

    val x = (imageWidth * xRate).toInt()
    val y = (imageHeight * yRate).toInt()

    val w = (imageWidth * wRate).toInt()
    val h = (imageHeight * hRate).toInt()

    val cutImage = bufferedImage.getSubimage(x, y, w, h)
    ImageIO.write(cutImage, this.getFileType(), out)
    return out.toByteArray()
}

/**
 * 判断是否为音频文件
 */
fun String.isAudioFile(): Boolean {
    val extension = this.substringAfterLast('.', "").lowercase()
    val audioExtensions = setOf(
        "mp3", "wav", "aac", "flac", "ogg", "m4a",
        "aiff", "wma", "amr", "mid", "midi", "opus"
    )
    return extension in audioExtensions
}