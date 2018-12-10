package com.musixise.musixisebox.shop.repository

import com.musixise.musixisebox.shop.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>