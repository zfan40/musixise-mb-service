package com.musixise.musixisebox.shop.service.impl

import com.alibaba.fastjson.JSON
import com.musixise.musixisebox.api.exception.MusixiseException
import com.musixise.musixisebox.server.aop.MusixiseContext
import com.musixise.musixisebox.server.repository.WorkRepository
import com.musixise.musixisebox.shop.domain.BoxInfo
import com.musixise.musixisebox.shop.domain.Order
import com.musixise.musixisebox.shop.domain.Product
import com.musixise.musixisebox.shop.domain.QOrder
import com.musixise.musixisebox.shop.repository.AddressRepository
import com.musixise.musixisebox.shop.repository.OrderRepository
import com.musixise.musixisebox.shop.repository.ProductRepository
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderListQueryVO
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderVO
import com.musixise.musixisebox.shop.rest.web.vo.req.PayVO
import com.musixise.musixisebox.shop.service.IOrderService
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*
import javax.annotation.Resource

@Component
class IOrderServiceImpl : IOrderService {

    @Resource
    private lateinit var orderRepository: OrderRepository

    @Resource
    lateinit var productRepository: ProductRepository

    @Resource
    lateinit var workRepository: WorkRepository

    @Resource
    lateinit var addressRepository: AddressRepository

    /**
     * 下单
     */
    override fun create(orderVO: OrderVO): Long {

        //获取产品信息
        val product = productRepository.findById(orderVO.pid)

        if (!product.isPresent) {
            throw MusixiseException("不存在的产品");
        }

        var currentUid = MusixiseContext.getCurrentUid()

        //地址是否存在
        addressRepository.findById(orderVO.addressId).map {

            if (!it.userId.equals(currentUid)) {
                throw MusixiseException("地址不存在02");
            }

        }.orElseThrow {
            throw MusixiseException("地址不存在01");
        }

        val order = Order(price = totalPrice(product.get().price, orderVO.amount),
            userId = currentUid, status = 0,
            content = getProductContent(orderVO.wid, product.get()),
            amount = orderVO.amount,
            shipTime = Date(),
            message = orderVO.message,
            address = orderVO.addressId)
        orderRepository.save(order);

        return order.id

    }

    /**
     * 获取当前用户订单列表
     */
    override fun myOrderList(orderListQueryVO: OrderListQueryVO) : Page<Order> {

        val sort = Sort(Sort.Direction.DESC, "id")
        val pageable = PageRequest.of(orderListQueryVO.page - 1, orderListQueryVO.size, sort)

        var currentUid = MusixiseContext.getCurrentUid()

        val qOrder = QOrder.order
        val builder = BooleanBuilder()


        builder.and(qOrder.userId.eq(currentUid))
        if (orderListQueryVO != null && orderListQueryVO.status != null) {
            builder.and(qOrder.status.eq(orderListQueryVO.status))
        }

        return orderRepository.findAll(builder, pageable)
    }

    fun totalPrice(price: BigDecimal, amount: Long) : BigDecimal {
        return price.multiply(BigDecimal(amount));
    }

    override fun pay(payVO: PayVO): Boolean {

        return true;
    }

    /**
     * 构建商品扩展信息
     */
    fun getProductContent(wid: Long, product: Product) : String {
        val one = workRepository.findById(wid)
        if (one.isPresent) {
            val work = one.get();
            val boxInfo = BoxInfo(work.id, product, work.title, work.userId, work.url)
            return JSON.toJSONString(boxInfo);
        }

        throw MusixiseException("未找到作品信息");
    }

    override fun get(orderId: Long): Order {
        return orderRepository.findById(orderId).orElseThrow {
            throw MusixiseException("不存在的订单号 ${orderId}");
        }
    }

    override fun update(order: Order) {
       orderRepository.save(order)
    }
}