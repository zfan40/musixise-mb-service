package com.musixise.musixisebox.shop.rest.admin.vo

import com.musixise.musixisebox.shop.domain.Address
import com.musixise.musixisebox.shop.rest.web.vo.resp.BoxInfoVO
import java.math.BigDecimal
import java.util.*

data class OrderDetailVO(var id: Long=0,
                         var price : BigDecimal?=null,
                         var status: Long=0,
                         var shipTime: Date?=null,
                         var confirmTime: Date?=null,
                         val userId: Long=0,
                         var amount: Long=0,
                         var product: BoxInfoVO?=null,
                         var address: Address?=null)