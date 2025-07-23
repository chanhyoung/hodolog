package com.hodolog.api.controller.shop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.request.shop.OrderCreate;
import com.hodolog.api.request.shop.OrderSearch;
import com.hodolog.api.response.shop.OrderResponse;
import com.hodolog.api.service.shop.OrderService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/shop/order")
    public void order (@RequestBody @Valid OrderCreate orderCreate) {
        log.info("orderCreate: {}", orderCreate);
        orderService.order(
                orderCreate.getMemberId(),
                orderCreate.getItemId(),
                orderCreate.getCount()
        );
    }

    @GetMapping("/shop/orders/v1")
    public List<OrderResponse> orderListV1(OrderSearch orderSearch) {
        log.info("orderSearch: {}", orderSearch);

        List<Order> orders = orderService.findOrdersV1(orderSearch);
        return orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());
    }

    @GetMapping("/shop/orders/v2")
    public List<OrderResponse> orderListV2(OrderSearch orderSearch) {
        log.info("orderSearch: {}", orderSearch);
        return orderService.findOrdersV2(orderSearch);
    }

    @GetMapping("/shop/orders/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        log.info("Canceling order with ID: {}", orderId);
        orderService.cancelOrder(orderId);
    }
}
