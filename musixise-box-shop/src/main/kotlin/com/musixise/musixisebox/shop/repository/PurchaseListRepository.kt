package com.musixise.musixisebox.shop.repository

import com.musixise.musixisebox.shop.domain.Purchase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface PurchaseListRepository : JpaRepository<Purchase, Long>, QuerydslPredicateExecutor<Purchase>