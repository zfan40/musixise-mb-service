package com.musixise.musixisebox.shop.service.impl

import com.github.wxpay.sdk.WXPay
import com.github.wxpay.sdk.WXPayConstants
import com.github.wxpay.sdk.WXPayUtil
import com.musixise.musixisebox.api.exception.MusixiseException
import com.musixise.musixisebox.server.aop.MusixiseContext
import com.musixise.musixisebox.server.config.pay.MyWxConfig
import com.musixise.musixisebox.server.repository.UserBindRepository
import com.musixise.musixisebox.shop.enums.OrderEnum
import com.musixise.musixisebox.shop.enums.ProductTypeEnum
import com.musixise.musixisebox.shop.manager.PurchaseListManager
import com.musixise.musixisebox.shop.repository.OrderRepository
import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO
import com.musixise.musixisebox.shop.service.IOrderService
import com.musixise.musixisebox.shop.service.IPayService
import com.musixise.musixisebox.shop.utils.OrderUtil
import com.musixise.musixisebox.shop.utils.OrderUtil.getBoxInfo
import org.apache.commons.lang.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*
import javax.annotation.Resource

@Component("ipayServiceImpl")
class IPayServiceImpl : IPayService {

    @Resource
    lateinit var config: MyWxConfig

    @Resource
    private lateinit var iOrderService: IOrderService;

    @Resource
    private lateinit var orderRepository: OrderRepository

    @Resource
    private lateinit var userBindRepository: UserBindRepository

    @Resource
    private lateinit var purchaseListManager: PurchaseListManager

    private val logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        val inSandbox = false;
        var signType = WXPayConstants.SignType.MD5
    }


    @Throws(Exception::class)
    override fun getPayInfo(orderId: Long): WCPayRequestVO {

        //参数需要重新进行签名计算，参与签名的参数为：appId、timeStamp、nonceStr、package、signType，参数区分大小写。

        val wxpay = WXPay(config, false, inSandbox)


        if (inSandbox) {
            //计算签名
            config?.key = retrieveSandboxSignKey(wxpay) ?: throw MusixiseException("获取沙箱密钥失败")
        }

        val prepayId = getPrepayId(orderId)

        val reqData = HashMap<String, String>()
        reqData["appId"] = config!!.appID
        reqData["nonceStr"] = WXPayUtil.generateNonceStr()
        reqData["signType"] = "MD5"
        reqData["timeStamp"] = WXPayUtil.getCurrentTimestamp().toString()
        reqData["package"] = "prepay_id=" + prepayId!!

        val signature = WXPayUtil.generateSignature(reqData, config?.key, signType)

        val wcPayRequestVO = WCPayRequestVO()

        wcPayRequestVO.appId = config!!.appID
        wcPayRequestVO.timeStamp = reqData["timeStamp"]
        wcPayRequestVO.nonceStr = reqData["nonceStr"]
        wcPayRequestVO.packageStr = reqData["package"]
        wcPayRequestVO.signType = "MD5"
        wcPayRequestVO.paySign = signature

        return wcPayRequestVO
    }

    fun retrieveSandboxSignKey(wxPay: WXPay): String? {

        try {
            val params = HashMap<String, String>()
            params["mch_id"] = config!!.getMchID()
            params["nonce_str"] = WXPayUtil.generateNonceStr()
            params["sign"] = WXPayUtil.generateSignature(params, config!!.getKey())
            val strXML = wxPay.requestWithoutCert(
                "/sandboxnew/pay/getsignkey",
                params, config!!.getHttpConnectTimeoutMs(), config!!.getHttpReadTimeoutMs()
            )
            if (StringUtils.isBlank(strXML)) {
                return null
            }
            logger.info("strXML ${strXML}")
            val result = WXPayUtil.xmlToMap(strXML)
            logger.info("retrieveSandboxSignKey:$result")
            return if ("SUCCESS" == result["return_code"]) {
                result["sandbox_signkey"]
            } else null
        } catch (e: Exception) {
            logger.error("获取sandbox_signkey异常", e)
            return null
        }

    }

    override fun getPayNotify(xmlData: String): String? {

        logger.info("getPayNotify request {}", xmlData)
        var result: String? = null

        if (StringUtils.isEmpty(xmlData)) {
            //xml为空
            logger.warn("xml of wechat pay is empty")
            return null
        }

        try {

            val map = WXPayUtil.xmlToMap(xmlData)
            logger.info("getPayNotify {}", map)
            val appid = map.getOrDefault("appid", "")
            val bank_type = map.getOrDefault("bank_type", "")
            val cash_fee = map.getOrDefault("cash_fee", "")
            val device_info = map.getOrDefault("device_info", "")
            val fee_type = map.getOrDefault("fee_type", "")
            val is_subscribe = map.getOrDefault("is_subscribe", "")
            val mch_id = map.getOrDefault("mch_id", "")
            val nonce_str = map.getOrDefault("nonce_str", "")
            val openid = map.getOrDefault("openid", "")
            val out_trade_no = map.getOrDefault("out_trade_no", "")
            val result_code = map.getOrDefault("result_code", "")
            val return_code = map.getOrDefault("return_code", "")
            val sign = map.getOrDefault("sign", "")
            val time_end = map.getOrDefault("time_end", "")
            val total_fee = map.getOrDefault("total_fee", "")
            val trade_type = map.getOrDefault("trade_type", "")
            val transaction_id = map.getOrDefault("transaction_id", "")

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

            if (localSign.equals(sign)) {
                if (result_code == "SUCCESS" && return_code == "SUCCESS") {
                    //业务模块处理点
                    logger.info("wechat notify done. " + date.toString())

                    val orderId = OrderUtil.getOrderId(out_trade_no)
                    payDone(orderId)


                    //正确的结果要分上面几个不一样返回，不能这样只返回一个
                    result = ("<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ")

                    return result

                } else {
                    //返回不是SUCCESS
                    logger.error("wechat notify reponse error: " + date.toString())
                    return null
                }
            } else {

                //签名失败
                logger.error("wechat notify sign error: " + date.toString())
                return null

            }

        } catch (e: Exception) {
            //转化XML错误
            logger.error("wechat notify format error. ", e.toString())
            e.printStackTrace()
            return null
        }


    }

    fun payDone(orderId: Long) : Boolean {
        val order = iOrderService.get(orderId)

        val boxInfo = getBoxInfo(order)

        purchaseListManager.create(userId = order.userId,
            pid = boxInfo.pid,
            wid = boxInfo.wid,
            orderId = orderId)

        when(order.status) {
            OrderEnum.PEDDING_PAY.status -> {
                //已支付，待发货
                if (order.productType.equals(ProductTypeEnum.MUSIX_DOWNLOAD.type)) {
                    order.status = OrderEnum.DONE.status
                } else {
                    order.status = OrderEnum.WAIT_DELIVER_GOODS.status
                }
                order.confirmTime = Date()
                iOrderService.update(order)
            }

            OrderEnum.DONE.status -> return true
            else -> return true
        }

        return true
    }

    @Throws(Exception::class)
    override fun getPrepayId(orderId: Long): String? {

        val wxpay = WXPay(config, false, inSandbox)

        //获取订单
        val order = iOrderService.get(orderId)

        when {
            !MusixiseContext.getCurrentUid().equals(order.userId) -> throw MusixiseException("支付异常01")
            order.status > 1 -> throw MusixiseException("支付异常02")
        }

        val boxInfo = getBoxInfo(order);

        val data = HashMap<String, String>()
        data["body"] = boxInfo.productName + " " + boxInfo.workName
        data["out_trade_no"] = OrderUtil.genOrderId(MusixiseContext.getCurrentUid(), orderId)
        //data["device_info"] = ""
        data["fee_type"] = "CNY"
        //微信支付不允许出现小数点，金额单位是分
        data["total_fee"] = Math.round(order.price.multiply(BigDecimal(100)).toDouble()).toString()
        data["spbill_create_ip"] = MusixiseContext.getRemoteIp();
        data["notify_url"] = config!!.payNotifyUrl
        data["trade_type"] = "JSAPI"  // 此处指定为扫码支付
        //data["product_id"] = orderId.toString()
        data["openid"] = getOpenId()

        logger.info("unifiedOrder prepare:"+data.toString())
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

    fun getOpenId() : String {
        val currentUid = MusixiseContext.getCurrentUid()
        val userBind = userBindRepository.findByUserIdAndProvider(currentUid, "wechat").orElseThrow {
            throw MusixiseException("还未绑定微信账号");
        }
        return userBind.openId;

    }
}
