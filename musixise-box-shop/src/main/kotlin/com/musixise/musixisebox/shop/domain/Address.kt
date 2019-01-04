package com.musixise.musixisebox.shop.domain

import javax.persistence.*

@Entity
@Table(name = "mu_address")
data class Address(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?=null,
                   var userId: Long,
                   var userName: String?=null,
                   var postalCode: String?=null,
                   var provinceName: String?=null,
                   var cityName: String?=null,
                   var countryName: String?=null,
                   var detailInfo: String?=null,
                   var nationalCode: String?=null,
                   var telNumber: String?=null
                   )