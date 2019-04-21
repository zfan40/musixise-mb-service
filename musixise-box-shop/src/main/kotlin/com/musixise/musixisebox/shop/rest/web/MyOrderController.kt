package com.musixise.musixisebox.shop.rest.web

import com.google.gson.Gson
import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixisePageResponse
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.server.aop.MusixiseContext
import com.musixise.musixisebox.shop.domain.MusixDownloadInfo
import com.musixise.musixisebox.shop.enums.ProductTypeEnum
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderListQueryVO
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderVO
import com.musixise.musixisebox.shop.rest.web.vo.req.PayVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.BoxInfoVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.MyOrderVO
import com.musixise.musixisebox.shop.service.IOrderService
import com.musixise.musixisebox.shop.utils.OrderUtil
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/shop/orders")
class MyOrderController {

    @Resource
    lateinit var iOrderService: IOrderService;

//    @RequestMapping("/create", consumes=arrayOf(MediaType.APPLICATION_JSON_VALUE)
//        , produces =  arrayOf(MediaType.APPLICATION_JSON_VALUE), method = arrayOf(RequestMethod.POST))
    @PostMapping("/create")
    @AppMethod(isLogin = true)
    fun create(@Valid @RequestBody orderVO: OrderVO) : MusixiseResponse<String> {

        val orderId = iOrderService.create(orderVO)
        return MusixiseResponse<String>(ExceptionMsg.SUCCESS,
            OrderUtil.genOrderId(MusixiseContext.getCurrentUid(), orderId))
    }

    @PostMapping("/pay")
    @AppMethod(isLogin = true)
    fun pay(@Valid @RequestBody payVO: PayVO) : MusixiseResponse<Boolean> {

        val pay = iOrderService.pay(payVO)
        return MusixiseResponse<Boolean>(ExceptionMsg.SUCCESS, pay);
    }

    @GetMapping("/myOrderList")
    @AppMethod(isLogin = true)
    fun myOrderList(@Valid orderListQueryVO: OrderListQueryVO) : MusixisePageResponse<List<MyOrderVO>> {

        val myOrderList = iOrderService.myOrderList(orderListQueryVO)

        val orderList = ArrayList<MyOrderVO>()

        myOrderList.content.forEach{

            var content:Any? = null
            try {

                when(it.productType) {
                    ProductTypeEnum.MUSIX_BOX.type -> {
                        content = Gson().fromJson(it.content.toString(), BoxInfoVO::class.java)
                    }

                    ProductTypeEnum.MUSIX_DOWNLOAD.type -> {
                        content = Gson().fromJson(it.content.toString(), MusixDownloadInfo::class.java)
                    }

                }

            } catch (e: Exception) {
                println(it.content)
            }

            val orderIdStr = OrderUtil.genOrderId(it.userId, it.id, it.shipTime);
            val myOrderVO = MyOrderVO(
                it.id,
                it.price,
                orderIdStr,
                it.status,
                it.shipTime,
                it.confirmTime,
                it.userId,
                it.amount,
                content,
                it.message,
                it.address);
            orderList.add(myOrderVO)
        }

        return MusixisePageResponse(orderList, myOrderList.totalElements, orderListQueryVO.size, orderListQueryVO.page)

    }
}
