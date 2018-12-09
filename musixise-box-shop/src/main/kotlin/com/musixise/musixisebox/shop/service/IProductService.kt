package com.musixise.musixisebox.shop.service

import com.musixise.musixisebox.shop.domain.Product

interface IProductService {
    fun queryProductList() : List<Product>
}