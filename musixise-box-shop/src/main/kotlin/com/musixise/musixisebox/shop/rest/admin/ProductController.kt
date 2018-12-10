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
@RequestMapping("/api/v1/admin/shop/products")
class ProductController {

    @Resource
    lateinit var productService: IProductService

    @Resource
    lateinit var productRepository: ProductRepository

    @GetMapping("")
    @AppMethod
    fun queryProductList() : MusixiseResponse<List<Product>> {
       return MusixiseResponse(productService.queryProductList())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody product: Product) : MusixiseResponse<Long> {
        productRepository.save(product)
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, product.id)
    }

    @PutMapping("")
    fun update(@Valid @RequestBody product: Product) : MusixiseResponse<String> {
        productRepository.save(product)
        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) : MusixiseResponse<String> {
        productRepository.deleteById(id);
        return MusixiseResponse(ExceptionMsg.SUCCESS)
    }


}