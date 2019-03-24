package com.musixise.musixisebox.shop.domain

import com.musixise.musixisebox.server.domain.AbstractEntity
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mu_order")
data class Order(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long=0,
                 var price : BigDecimal,
                 var status: Long,
                 var shipTime: Date?=null,
                 var confirmTime: Date?=null,
                 val userId: Long,
                 var amount: Long,
                 var content: String?=null,
                 var message: String?=null,
                 var address: Long = 0) : AbstractEntity()