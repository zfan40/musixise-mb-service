package com.musixise.musixisebox.shop.domain

import com.musixise.musixisebox.server.domain.AbstractEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "mu_product")
data class Product(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
                   var category: Long,
                   var name: String,
                   var intro: String,
                   var price: BigDecimal,
                   var previewPic: String,
                   var previewVideo: String,
                   var status: Long) : AbstractEntity()