package com.musixise.musixisebox.shop.utils

import org.apache.commons.lang.StringUtils
import java.text.SimpleDateFormat
import java.util.*


object OrderUtil {

    /**
     * 生成订单号
     * 20190106222026003b9ac9ff02540be3ff
     */
    fun genOrderId(userId: Long, orderId: Long): String {

        val sdf = SimpleDateFormat("yyyyMMddHH")
        val format = sdf.format(Date())
        val userIdHex  = StringUtils.leftPad(java.lang.Long.toHexString(userId), 10, "0")
        val orderIdHex = StringUtils.leftPad(java.lang.Long.toHexString(orderId), 10, "0")
        return "${format}${userIdHex}${orderIdHex}"
    }

    /**
     * 解析订单号
     */
    fun getOrderId(orderId: String): Long {
        return java.lang.Long.parseLong(orderId.substring(20).replaceFirst("^0*", ""), 16)
    }

    /**
     * 解析出用户ID
     */
    fun getUserId(orderId: String): Long {
        return java.lang.Long.parseLong(orderId.substring(10, 20).replaceFirst("^0*", ""), 16)
    }

}