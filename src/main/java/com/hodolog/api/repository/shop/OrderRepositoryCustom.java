package com.hodolog.api.repository.shop;

import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.request.shop.OrderSearch;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrders(OrderSearch orderSearch);
}
