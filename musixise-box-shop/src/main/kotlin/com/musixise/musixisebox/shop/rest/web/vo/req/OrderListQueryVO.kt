package com.musixise.musixisebox.shop.rest.web.vo.req

data class OrderListQueryVO(val status: Long?=null, val page: Int=1, val size: Int=10)