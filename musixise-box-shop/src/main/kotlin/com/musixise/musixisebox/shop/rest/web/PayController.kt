package com.musixise.musixisebox.shop.rest.web

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.shop.rest.web.vo.req.pay.UnifiedorderVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO
import com.musixise.musixisebox.shop.service.IPayService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("/api/v1/shop/pay")

class PayController {

    @Resource
    private val PayServiceImpl: IPayService? = null

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ApiOperation(value = "下单", notes = "")
    @AppMethod(isLogin = true)
    @PostMapping("/unifiedorder")
    fun unifiedorder(unifiedorderVO: UnifiedorderVO): MusixiseResponse<WCPayRequestVO> {

        val payInfo = PayServiceImpl!!.getPayInfo(unifiedorderVO.orderId)
        return MusixiseResponse(ExceptionMsg.SUCCESS, payInfo)
    }

    @ApiOperation(value = "支付通知", notes = "")
    @PostMapping("/notify")
    @AppMethod
    fun payNotify(@RequestBody xmlData: String): String? {
        return PayServiceImpl?.getPayNotify(xmlData)
    }
}