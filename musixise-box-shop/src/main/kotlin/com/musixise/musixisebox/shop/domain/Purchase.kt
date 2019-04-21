package com.musixise.musixisebox.shop.domain

import com.musixise.musixisebox.server.domain.AbstractEntity
import javax.persistence.*

@Entity
@Table(name = "mu_purchase_lit")
data class Purchase(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long=0,
                   var userId: Long,
                   var pid: Long=0,
                   var wid: Long=0,
                   var orderId: Long=0) : AbstractEntity()
