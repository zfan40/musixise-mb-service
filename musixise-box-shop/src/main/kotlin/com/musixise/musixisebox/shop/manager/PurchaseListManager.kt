package com.musixise.musixisebox.shop.manager

import com.musixise.musixisebox.shop.domain.Purchase
import com.musixise.musixisebox.shop.domain.QPurchase
import com.musixise.musixisebox.shop.repository.PurchaseListRepository
import com.querydsl.core.BooleanBuilder
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class PurchaseListManager {

    @Resource
    private lateinit var purchaseListRepository: PurchaseListRepository

    fun create(userId: Long, pid: Long, wid: Long, orderId: Long) {
        val purchase = Purchase(
            userId = userId,
            pid = pid,
            wid = wid,
            orderId = orderId
        )
        purchaseListRepository.save(purchase)
    }

    fun exists(userId: Long, pid: Long, wid: Long) :Boolean {
        val purchase = QPurchase.purchase
        val booleanBuilder = BooleanBuilder()
        booleanBuilder.and(purchase.userId.eq(userId))
        booleanBuilder.and(purchase.pid.eq(pid))
        booleanBuilder.and(purchase.wid.eq(wid))
        return purchaseListRepository.exists(booleanBuilder)
    }
}