package com.musixise.musixisebox.shop.manager

import com.musixise.musixisebox.shop.domain.Address
import com.musixise.musixisebox.shop.domain.QAddress
import com.musixise.musixisebox.shop.repository.AddressRepository
import com.querydsl.core.BooleanBuilder
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class AddressManager {

    @Resource
    private lateinit var addressRepository: AddressRepository

    fun getAddressMap(ids: List<Long>) : Map<Long?, Address> {

        val address = QAddress.address
        val booleanBuilder = BooleanBuilder()
        val builder = booleanBuilder.and(address.id.`in`(ids))

        val findAll = addressRepository.findAll(builder)
        return findAll.associateBy({ it.id }, { it })
    }
}