package com.musixise.musixisebox.shop.service

import com.musixise.musixisebox.shop.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IProductService {
    fun queryProductList(pageable: Pageable) : Page<Product>
}