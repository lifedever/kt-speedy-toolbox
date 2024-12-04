package kt.speedy.toolbox.util

import cn.hutool.json.JSONObject
import kotlin.math.floor
import kotlin.math.pow

/**
 * 百分比工具类
 */
object PercentKit {
    /**
     * 修复百分比结果相加不等于100的问题
     * @param arr 原始值的数组 如 [10, 32, 55, 22]
     * @param sum 原始值之和 10+32+55+22=119
     * @param idx 当前位置索引
     * @param scale 精度
     *
     * @return 返回结果乘以100后的值，比如 0.333 返回 33.3
     */
    fun getPercentValue(arr: List<Double>, sum: Double, idx: Int, scale: Int): Double {
        var sum = sum
        if (arr.size - 1 < idx) {
            return 0.0.round(scale)
        }
        //求和
        if (sum <= 0) {
            for (i in arr.indices) {
                sum += arr[i]
            }
        }
        //10的2次幂是100，用于计算精度。
        val digits = 10.0.pow(scale.toDouble())
        //扩大比例100
        val votesPerQuota = DoubleArray(arr.size)
        for (i in arr.indices) {
            val `val` = arr[i] / sum * digits * 100
            votesPerQuota[i] = `val`
        }
        //总数,扩大比例意味的总数要扩大
        val targetSeats = digits * 100
        //再向下取值，组成数组
        val seats = DoubleArray(arr.size)
        for (i in votesPerQuota.indices) {
            seats[i] = floor(votesPerQuota[i])
        }
        //再新计算合计，用于判断与总数量是否相同,相同则占比会100%
        var currentSum = 0.0
        for (i in seats.indices) {
            currentSum += seats[i]
        }
        //余数部分的数组:原先数组减去向下取值的数组,得到余数部分的数组
        val remainder = DoubleArray(arr.size)
        for (i in seats.indices) {
            remainder[i] = votesPerQuota[i] - seats[i]
        }
        while (currentSum < targetSeats) {
            var max = 0.0
            var maxId = 0
            for (i in remainder.indices) {
                if (remainder[i] > max) {
                    max = remainder[i]
                    maxId = i
                }
            }
            //对最大项余额加1
            ++seats[maxId]
            //已经增加最大余数加1,则下次判断就可以不需要再判断这个余额数。
            remainder[maxId] = 0.0
            //总的也要加1,为了判断是否总数是否相同,跳出循环。
            ++currentSum
        }
        // 这时候的seats就会总数占比会100%
        return seats[idx] / digits
    }

    fun getPercentValue(arr: List<Double>, idx: Int, scale: Int) : Double {
        var sum = arr.sum()
        return getPercentValue(arr, sum, idx, scale)
    }
     fun getPercentValue(arr: List<Double>,scale: Int) : List<Double> {
         var sum = arr.sum()
         return arr.mapIndexed { index, item ->
             getPercentValue(arr, sum, index, scale)
         }
    }
}

fun main() {
    val arr = listOf(10.0, 32.0, 55.0, 22.0)



    val percentArr = arr.mapIndexed {index, item ->

        PercentKit.getPercentValue(
            arr = arr,
            sum = 119.0,
            idx = index,
            scale = 1
        )
    }

    println(percentArr)
}