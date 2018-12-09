package com.musixise.musixisebox.shop.repository

import com.musixise.musixisebox.shop.domain.Product
import org.springframework.data.jpa.repository.JpaRepository


interface ProductRepository : JpaRepository<Product, Long>