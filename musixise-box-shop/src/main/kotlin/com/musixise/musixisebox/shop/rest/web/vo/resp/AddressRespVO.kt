package com.musixise.musixisebox.shop.rest.web.vo.resp

data class AddressRespVO(val id: Long?=null,
                         var userId: Long?=null,
                         var userName: String?=null,
                         var postalCode: String?=null,
                         var provinceName: String?=null,
                         var cityName: String?=null,
                         var countryName: String?=null,
                         var detailInfo: String?=null,
                         var nationalCode: String?=null,
                         var telNumber: String?=null)