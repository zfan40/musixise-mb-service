package com.musixise.musixisebox.shop.rest.web.vo.req

data class AddressVO(
                   var userName: String,
                   var postalCode: String,
                   var provinceName: String,
                   var cityName: String,
                   var countryName: String,
                   var detailInfo: String,
                   var nationalCode: String,
                   var telNumber: String
)