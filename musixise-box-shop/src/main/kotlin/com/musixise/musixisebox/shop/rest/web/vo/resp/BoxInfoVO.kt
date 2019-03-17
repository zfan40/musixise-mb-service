package com.musixise.musixisebox.shop.rest.web.vo.resp

data class BoxInfoVO(val wid: Long=0,
                     val product: ProductVO?=null,
                     val title: String?=null,
                     val userId: Long=0,
                     val url: String?=null)