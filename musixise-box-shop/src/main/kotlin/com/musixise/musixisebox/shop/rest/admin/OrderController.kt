package com.musixise.musixisebox.shop.rest.admin

import com.google.gson.Gson
import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.exception.MusixiseException
import com.musixise.musixisebox.api.result.MusixisePageResponse
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.utils.CommonUtil
import com.musixise.musixisebox.shop.domain.Order
import com.musixise.musixisebox.shop.domain.QOrder
import com.musixise.musixisebox.shop.manager.AddressManager
import com.musixise.musixisebox.shop.repository.AddressRepository
import com.musixise.musixisebox.shop.repository.OrderRepository
import com.musixise.musixisebox.shop.rest.admin.vo.OrderDetailVO
import com.musixise.musixisebox.shop.rest.web.vo.resp.BoxInfoVO
import com.querydsl.core.BooleanBuilder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors
import javax.annotation.Resource
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/admin/shop/orders")
class OrderController {

    @Resource
    private lateinit var orderRepository: OrderRepository

    @Resource
    private lateinit var addressRepository: AddressRepository

    @Resource
    private lateinit var addressManager: AddressManager

    @GetMapping("")
    fun getList(@RequestParam(required = false) status: Long?,
                @RequestParam(defaultValue = "1") page: Int,
                @RequestParam(defaultValue = "10") size: Int) : MusixisePageResponse<List<OrderDetailVO>> {

        val orderQ = QOrder.order
        val builder = BooleanBuilder()
        builder.and(orderQ.id.longValue().gt(0))
        status?.let {
            builder.and(orderQ.status.eq(it))
        }

        val sort = Sort(Sort.Order(Sort.Direction.DESC, "id"))
        val pageable = PageRequest.of(page - 1, size, sort)

        val orderVOList = arrayListOf<OrderDetailVO>()

        val order = orderRepository.findAll(builder, pageable);

        val addressIds = order.stream().map(Order::address).collect(Collectors.toList())

        val addressMap = addressManager.getAddressMap(addressIds)

        order.content.forEach {
            val orderVO = OrderDetailVO()
            CommonUtil.copyPropertiesIgnoreNull(it, orderVO)

            try {
                orderVO.product = Gson().fromJson(it.content.toString(), BoxInfoVO::class.java)
            } catch (e: Exception) {

            }

            //assign address
            if (it.address > 0 && addressMap.containsKey(it.address)) {
                orderVO.address = addressMap.get(it.address)
            }

            orderVOList.add(orderVO)
        }

        return MusixisePageResponse(orderVOList, order.totalElements, size, page)
    }

    @PostMapping("")
    fun create(@Valid @RequestBody order: Order) : MusixiseResponse<Long> {
        order.shipTime = Date()
        orderRepository.save(order);
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, order.id)
    }

    @PutMapping("")
    fun update(@Valid @RequestBody order: Order) : MusixiseResponse<String> {
        orderRepository.findById(order.id).map {
            CommonUtil.copyPropertiesIgnoreNull(order, it)
            orderRepository.save(it);
        }.orElseThrow {
            throw MusixiseException("订单不存在");
        }

        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }

    @DeleteMapping("")
    fun delete(@PathVariable id: Long) : MusixiseResponse<String> {
        orderRepository.deleteById(id)
        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }
}

