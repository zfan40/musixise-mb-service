package com.musixise.musixisebox.shop.domain

import com.musixise.musixisebox.server.domain.AbstractEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "mu_product")
data class Product(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
                   val category: Long,
                   val name: String,
                   val intro: String,
                   val price: BigDecimal,
                   val status: Long) : AbstractEntity()