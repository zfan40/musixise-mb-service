package com.musixise.musixisebox.shop.service

import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO
import javax.servlet.http.HttpServletRequest

interface IPayService {
    @Throws(Exception::class)
    fun getPayInfo(productId: Long): WCPayRequestVO

    @Throws(Exception::class)
    fun getPrepayId(productId: Long): String?

    fun getPayNotify(request: HttpServletRequest): String?
}