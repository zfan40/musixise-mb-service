package com.musixise.musixisebox.shop.rest.web.vo.req

data class OrderVO (val pid: Long,
                    val wid: Long,
                    val amount: Long = 1,
                    var addressId: Long,
                    val message: String)