package com.musixise.musixisebox.shop.domain

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "mu_order ")
data class Order(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
                 val price : BigDecimal,
                 val status: Long,
                 var shipTime: Date,
                 val userId: Long,
                 val address: Long)