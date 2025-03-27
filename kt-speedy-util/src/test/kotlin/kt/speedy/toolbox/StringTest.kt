package kt.speedy.toolbox

import kt.speedy.toolbox.util.chunkedRealSizeByCharset
import kt.speedy.toolbox.util.findBetween
import kt.speedy.toolbox.util.ifNotPresentOrEqual
import kotlin.test.Test

class StringTest {

    @Test
    fun test1() {
        val str = "无".ifNotPresentOrEqual("无") {
            "【员工补充：ssssss】"
        }
        println(str)
    }

    @Test
    fun test2() {
        val arr = """
            【订购提醒】尊敬的客户，您好！您已于2024年10月10日17时28分40秒通过随销渠道成功办理了以下业务，合约期内，须持续使用中国移动网络，如申请办理销户、停机、携号转网等业务，需先进行解约。
            已订购：
            1、【顺差专属流量特惠包】将于2024年10月10日17时28分40秒生效。合约期至2025年09月30日23时59分59秒。每月10元，有效期12个月，每个月包含100分钟语音及30GB通用流量 ，到期自动退订。

            注：上述业务中，未明确业务失效时间的，均默认有效期至办理当年12月31日，到期后如未另行通知则自动延期一年，以此类推。
            我们百倍努力，只为您10分满意 【中国移动】
        """.trimIndent().chunked(240)

            arr.forEachIndexed { index, s ->
                println("【${index + 1}/${arr.size}】" + s)
            }
    }

    @Test
    fun test3() {
        val txt =
            "关于本人于2025年02月23日与中国移动通信集团新疆有限公司".findBetween("关于本人于", "与中国移动通信集团", false)
        println(txt)
    }
}