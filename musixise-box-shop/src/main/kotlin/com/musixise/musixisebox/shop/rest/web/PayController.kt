package com.musixise.musixisebox.shop.rest.web

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.shop.rest.web.vo.req.pay.UnifiedorderVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO
import com.musixise.musixisebox.shop.service.IPayService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/shop/pay")

class PayController {

    @Resource
    private val PayServiceImpl: IPayService? = null

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ApiOperation(value = "下单", notes = "")
    @AppMethod(isLogin = false)
    @PostMapping("/unifiedorder")
    fun unifiedorder(uid: Long?, unifiedorderVO: UnifiedorderVO): MusixiseResponse<WCPayRequestVO> {

        try {

            val payInfo = PayServiceImpl!!.getPayInfo(unifiedorderVO.orderId)
            return MusixiseResponse(ExceptionMsg.SUCCESS, payInfo)
        } catch (e: Exception) {
            logger.error("Exception do unifiedorder action: ", e)
            return MusixiseResponse(ExceptionMsg.FAILED)
        }

    }

    @ApiOperation(value = "支付通知", notes = "")
    @GetMapping("/notify")
    fun payNotify(request: HttpServletRequest): String? {
        return PayServiceImpl?.getPayNotify(request)
    }
}