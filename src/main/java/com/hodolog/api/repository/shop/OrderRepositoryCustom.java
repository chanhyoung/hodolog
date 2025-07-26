package com.hodolog.api.repository.shop;

import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.request.shop.OrderSearch;
import com.hodolog.api.response.shop.OrderItemResponse;
import com.hodolog.api.response.shop.OrderResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface OrderRepositoryCustom {

    List<Order> findOrdersV1(OrderSearch orderSearch);
    List<OrderResponse> findOrdersV2(OrderSearch orderSearch);
    Page<OrderResponse> findOrdersPaging(OrderSearch orderSearch);
    List<OrderItemResponse> findOrderItems(List<Long> orderIds);
}
