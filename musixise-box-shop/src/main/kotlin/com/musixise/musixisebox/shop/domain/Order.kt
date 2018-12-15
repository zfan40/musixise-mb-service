package com.musixise.musixisebox.shop.domain

import com.musixise.musixisebox.server.domain.AbstractEntity
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mu_order")
data class Order(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?=null,
                 val price : BigDecimal,
                 val status: Long,
                 var shipTime: Date?=null,
                 var confirmTime: Date?=null,
                 val userId: Long,
                 val amount: Long,
                 val content: String,
                 val address: Long = 0) : AbstractEntity()