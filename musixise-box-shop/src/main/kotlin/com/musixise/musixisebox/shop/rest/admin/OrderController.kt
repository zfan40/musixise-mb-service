package com.musixise.musixisebox.shop.rest.admin

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.shop.domain.Order
import com.musixise.musixisebox.shop.repository.OrderRepository
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.Resource
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/admin/shop/orders")
class OrderController {

    @Resource
    private lateinit var orderRepository: OrderRepository

    @GetMapping("")
    fun getList() : MusixiseResponse<List<Order>> {
        val findAll = orderRepository.findAll()
        return MusixiseResponse(findAll)
    }

    @PostMapping("")
    fun create(@Valid @RequestBody order: Order) : MusixiseResponse<Long> {
        order.shipTime = Date()
        orderRepository.save(order);
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, order.id)
    }

    @PutMapping("")
    fun update(@Valid @RequestBody order: Order) : MusixiseResponse<String> {
        orderRepository.save(order);
        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }

    @DeleteMapping("")
    fun delete(@PathVariable id: Long) : MusixiseResponse<String> {
        orderRepository.deleteById(id)
        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }
}

