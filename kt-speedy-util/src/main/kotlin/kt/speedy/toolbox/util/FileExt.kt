package kt.speedy.toolbox.util

import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

/**
 * 临时生成文件
 */
fun File.tempUse(block: () -> Unit) {
    block()
    this.delete()
}

/**
 * 判断是否为图片
 */
fun File.isImageFile(): Boolean {
    val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
    val extension = this.extension.lowercase()
    return imageExtensions.contains(extension)
}

fun String.filterFiles(): Boolean {
    val filter = arrayOf(
        "__MACOSX",
    )

    return filter.none { it == this } && !this.startsWith(".")
}

fun File.getFileType(): String {
    return this.name.substringAfterLast('.')
}

fun String.toFile(): File {
    return File(this)
}

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
