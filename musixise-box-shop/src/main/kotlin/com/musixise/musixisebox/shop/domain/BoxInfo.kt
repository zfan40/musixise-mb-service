package com.musixise.musixisebox.shop.domain

data class BoxInfo(val wid: Long,
                   val product: Product,
                   val title: String,
                   val userId: Long,
                   val url: String)