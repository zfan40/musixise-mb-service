package com.musixise.musixisebox.shop.repository

import com.musixise.musixisebox.shop.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

interface OrderRepository : JpaRepository<Order, Long>,QuerydslPredicateExecutor<Order>