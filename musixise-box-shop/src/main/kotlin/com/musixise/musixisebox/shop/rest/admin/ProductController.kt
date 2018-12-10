package com.musixise.musixisebox.shop.rest.admin

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.shop.domain.Product
import com.musixise.musixisebox.shop.repository.ProductRepository
import com.musixise.musixisebox.shop.service.IProductService
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/admin/shop")
class ProductController {

    @Resource
    lateinit var productService: IProductService

    @Resource
    lateinit var productRepository: ProductRepository

    @GetMapping("/products")
    @AppMethod
    fun queryProductList() : MusixiseResponse<List<Product>> {
       return MusixiseResponse(productService.queryProductList());
    }

    @PostMapping("/product")
    fun create(@Valid @RequestBody product: Product) : MusixiseResponse<Long> {
        productRepository.save(product);
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, product.id);
    }





}