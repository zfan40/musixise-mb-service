package com.musixise.musixisebox.shop.rest.web.vo.resp

import java.math.BigDecimal
import java.util.*

data class MyOrderVO(val id: Long?=null,
                     var price : BigDecimal,
                     var status: Long,
                     var shipTime: Date?=null,
                     var confirmTime: Date?=null,
                     val userId: Long,
                     var amount: Long,
                     var content: BoxInfoVO?=null,
                     var address: Long = 0)
