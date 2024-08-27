package kt.speedy.toolbox.util

import java.io.File

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