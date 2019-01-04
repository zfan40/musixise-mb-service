package com.musixise.musixisebox.shop.repository

import com.musixise.musixisebox.shop.domain.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface AddressRepository : JpaRepository<Address, Long>, QuerydslPredicateExecutor<Address>