package com.musixise.musixisebox.shop.service.impl

import com.alibaba.fastjson.JSON
import com.github.wxpay.sdk.WXPay
import com.github.wxpay.sdk.WXPayUtil
import com.musixise.musixisebox.api.exception.MusixiseException
import com.musixise.musixisebox.server.aop.MusixiseContext
import com.musixise.musixisebox.server.config.pay.MyWxConfig
import com.musixise.musixisebox.server.repository.UserBindRepository
import com.musixise.musixisebox.shop.domain.BoxInfo
import com.musixise.musixisebox.shop.domain.Order
import com.musixise.musixisebox.shop.enums.OrderEnum
import com.musixise.musixisebox.shop.repository.OrderRepository
import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO
import com.musixise.musixisebox.shop.service.IOrderService
import com.musixise.musixisebox.shop.service.IPayService
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.math.BigDecimal
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

@Component("ipayServiceImpl")
class IPayServiceImpl : IPayService {

    @Resource
    internal var config: MyWxConfig? = null

    @Resource
    private lateinit var iOrderService: IOrderService;

    @Resource
    private lateinit var orderRepository: OrderRepository

    @Resource
    private lateinit var userBindRepository: UserBindRepository

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(Exception::class)
    override fun getPayInfo(orderId: Long): WCPayRequestVO {

        //参数需要重新进行签名计算，参与签名的参数为：appId、timeStamp、nonceStr、package、signType，参数区分大小写。

        val wxpay = WXPay(config, false, true)
        val prepayId = getPrepayId(orderId)

        val reqData = HashMap<String, String>()
        reqData["appId"] = config!!.appID
        reqData["nonceStr"] = WXPayUtil.generateNonceStr()
        reqData["signType"] = "MD5"
        reqData["timeStamp"] = System.currentTimeMillis().toString()
        reqData["package"] = "prepay_id=" + prepayId!!

        //计算签名
        val signature = WXPayUtil.generateSignature(reqData, config!!.key)

        val wcPayRequestVO = WCPayRequestVO()

        wcPayRequestVO.appId = config!!.appID
        wcPayRequestVO.timeStamp = reqData["timeStamp"]
        wcPayRequestVO.nonceStr = reqData["nonceStr"]
        wcPayRequestVO.packageStr = reqData["package"]
        wcPayRequestVO.signType = "MD5"
        wcPayRequestVO.paySign = signature

        return wcPayRequestVO
    }

    override fun getPayNotify(request: HttpServletRequest): String? {

        var result: String? = null
        var inlength: String
        var notifyXml = ""
        try {

            request.reader.forEachLine {
                notifyXml += it
            }

        } catch (e: IOException) {
            //获取XML错误
            logger.error("Exception parse wechat notify data: " + request.toString())
        }

        if (StringUtils.isEmpty(notifyXml)) {
            //xml为空
            logger.warn("xml of wechat pay is empty")
            return null
        }

        try {

            val map = WXPayUtil.xmlToMap(notifyXml)
            logger.info("getPayNotify {}", map)
            val appid = map["appid"]
            val bank_type = map["bank_type"]
            val cash_fee = map["cash_fee"]
            val device_info = map["device_info"]
            val fee_type = map["fee_type"]
            val is_subscribe = map["is_subscribe"]
            val mch_id = map["mch_id"]
            val nonce_str = map["nonce_str"]
            val openid = map["openid"]
            val out_trade_no = map["out_trade_no"]
            val result_code = map["result_code"]
            val return_code = map["return_code"]
            val sign = map["sign"]
            val time_end = map["time_end"]
            val total_fee = map["total_fee"]
            val trade_type = map["trade_type"]
            val transaction_id = map["transaction_id"]

            val date = HashMap<String, String?>()
            date["appid"] = appid
            date["bank_type"] = bank_type
            date["cash_fee"] = cash_fee
            date["device_info"] = device_info
            date["fee_type"] = fee_type
            date["is_subscribe"] = is_subscribe
            date["mch_id"] = mch_id
            date["nonce_str"] = nonce_str
            date["openid"] = openid
            date["out_trade_no"] = out_trade_no
            date["result_code"] = result_code
            date["return_code"] = return_code
            date["time_end"] = time_end
            date["total_fee"] = total_fee
            date["trade_type"] = trade_type
            date["transaction_id"] = transaction_id

            var localSign: String? = null
            localSign = WXPayUtil.generateSignature(date, config!!.key)

            if (localSign == sign) {
                if (result_code == "SUCCESS" && return_code == "SUCCESS") {
                    //业务模块处理点
                    logger.info("wechat notify done. " + date.toString())

                } else {
                    //签名失败
                    logger.error("wechat notify sign error: " + date.toString())
                    return null
                }
            }

        } catch (e: Exception) {
            //转化XML错误
            logger.error("wechat notify format error. ", e.toString())
            e.printStackTrace()
            return null
        }

        //正确的结果要分上面几个不一样返回，不能这样只返回一个
        result = ("<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ")

        return result
    }

    @Throws(Exception::class)
    override fun getPrepayId(orderId: Long): String? {

        val wxpay = WXPay(config)

        //获取订单
        val order = iOrderService.get(orderId)

        when {
            !MusixiseContext.getCurrentUid().equals(order.userId) -> throw MusixiseException("支付异常01")
            !OrderEnum.UNPAY.status.equals(order.status) -> throw MusixiseException("支付异常02")
        }

        val boxInfo = getBoxInfo(order);

        val data = HashMap<String, String>()
        data["body"] = boxInfo.product.name + " " + boxInfo.title
        data["out_trade_no"] = orderId.toString()
        data["device_info"] = ""
        data["fee_type"] = "CNY"
        //微信支付不允许出现小数点，金额单位是分
        data["total_fee"] = Math.round(order.price.multiply(BigDecimal(100)).toDouble()).toString()
        data["spbill_create_ip"] = MusixiseContext.getRemoteIp();
        data["notify_url"] = "http://api.octave-love.com/api/v1/shop/pay/notify"
        data["trade_type"] = "JSAPI"  // 此处指定为扫码支付
        data["product_id"] = orderId.toString()
        data["openid"] = getOpenId()

        val resp = wxpay.unifiedOrder(data)
        if ("FAIL".equals(resp["return_code"]) || "FAIL".equals(resp["result_code"])) {
            logger.error("Exception do unifiedorder action: " + resp.toString())
            throw MusixiseException("支付异常03");
        } else {
            logger.info("wxpay.unifiedOrder {}", resp)
            //修改订单状态
            order.status=1L
            orderRepository.save(order)
            return (resp as java.util.Map<String, String>).getOrDefault("prepay_id", "")
        }

    }

    fun getBoxInfo(order: Order) : BoxInfo {
        return JSON.parseObject(order.content, BoxInfo::class.java)
    }

    fun getOpenId() : String {
        val currentUid = MusixiseContext.getCurrentUid()
        val userBind = userBindRepository.findByUserIdAndProvider(currentUid, "wechat").orElseThrow {
            throw MusixiseException("还未绑定微信账号");
        }
        return userBind.openId;

    }
}
