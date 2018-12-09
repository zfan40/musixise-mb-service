package com.musixise.musixisebox.shop.service.impl

import com.musixise.musixisebox.shop.domain.Product
import com.musixise.musixisebox.shop.repository.ProductRepository
import com.musixise.musixisebox.shop.service.IProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IProductServiceImpl : IProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    override fun queryProductList(): List<Product> {

        val productList = productRepository.findAll();
        return productList;
    }
}