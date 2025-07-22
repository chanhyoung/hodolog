package com.hodolog.api.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hodolog.api.domain.shop.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
