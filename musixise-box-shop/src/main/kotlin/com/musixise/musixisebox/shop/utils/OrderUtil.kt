package com.musixise.musixisebox.shop.utils

import com.google.gson.Gson
import com.musixise.musixisebox.shop.domain.MusixDownloadInfo
import com.musixise.musixisebox.shop.domain.Order
import com.musixise.musixisebox.shop.enums.ProductTypeEnum
import com.musixise.musixisebox.shop.rest.web.vo.resp.BoxInfoVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.ProductItem
import org.apache.commons.lang.StringUtils
import java.text.SimpleDateFormat
import java.util.*


object OrderUtil {

    /**
     * 生成订单号
     * 20190106222026003b9ac9ff02540be3ff
     */
    fun genOrderId(userId: Long, orderId: Long): String {
        return genOrderId(userId, orderId, Date());
    }

    fun genOrderId(userId: Long, orderId: Long, orderTime: Date?): String {
        val sdf = SimpleDateFormat("yyyyMMddHH")
        val format = sdf.format(orderTime)
        val userIdHex  = StringUtils.leftPad(java.lang.Long.toHexString(userId), 10, "0")
        val orderIdHex = StringUtils.leftPad(java.lang.Long.toHexString(orderId), 10, "0")
        return "${format}${userIdHex}${orderIdHex}"
    }

    /**
     * 解析订单号
     */
    fun getOrderId(orderId: String): Long {
        if (orderId.length<20) {
            return 0;
        } else {
            return java.lang.Long.parseLong(orderId.substring(20).replaceFirst("^0*", ""), 16)
        }
    }

    /**
     * 解析出用户ID
     */
    fun getUserId(orderId: String): Long {
        return java.lang.Long.parseLong(orderId.substring(10, 20).replaceFirst("^0*", ""), 16)
    }


    fun getBoxInfo(order: Order) : ProductItem {
        //return JSON.parseObject(order.content, BoxInfoVO::class.java)
        when(order.productType) {
            ProductTypeEnum.MUSIX_DOWNLOAD.type -> {

                val musixDownload = Gson().fromJson(order.content.toString(), MusixDownloadInfo::class.java)
                return ProductItem(wid = musixDownload.wid,
                    pid = musixDownload.product!!.id,
                    workName = musixDownload.title,
                    productName= musixDownload.product?.name)
            }

            else -> {
                var musixBox = Gson().fromJson(order.content.toString(), BoxInfoVO::class.java)
                return ProductItem(wid = musixBox.wid,
                    pid = musixBox.product!!.id,
                    workName = musixBox.title,
                    productName= musixBox.product?.name)
            }
        }
    }
}