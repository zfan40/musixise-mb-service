package com.musixise.musixisebox.shop.rest.web.vo.resp

import java.math.BigDecimal

data class ProductVO(val id: Long,
                     var category: Long,
                     var name: String,
                     var intro: String,
                     var price: BigDecimal,
                     var previewPic: String,
                     var previewVideo: String,
                     var status: Long)