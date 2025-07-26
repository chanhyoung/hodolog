package com.hodolog.api.service.shop;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.repository.shop.OrderRepository;
import com.hodolog.api.request.shop.OrderSearch;
import com.hodolog.api.response.shop.OrderItemResponse;
import com.hodolog.api.response.shop.OrderResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> orderListV1(@RequestBody @Valid OrderSearch orderSearch) {
        log.info("orderSearch: {}", orderSearch);

        List<Order> orders = findOrdersV1(orderSearch);
        return orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());
    }

    public List<OrderResponse> orderListV2(@RequestBody @Valid OrderSearch orderSearch) {
        log.info("orderSearch: {}", orderSearch);
        List<OrderResponse> result = findOrdersV2(orderSearch);
        List<Long> orderIds = result.stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());

        List<OrderItemResponse> orderItems = findOrderItems(orderIds);
        log.info("orderItems: {}", orderItems);

        Map<Long, List<OrderItemResponse>> orderItemsMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemResponse -> orderItemResponse.getOrderId()));
        log.info("orderItemsMap: {}", orderItemsMap);

        result.forEach(order -> order.setOrderItems(orderItemsMap.get(order.getId())));
        return result;
    }

    public Page<OrderResponse> orderListPaging(@RequestBody @Valid OrderSearch orderSearch) {
        log.info("orderSearch: {}", orderSearch);
        return orderRepository.findOrdersPaging(orderSearch);
    }

    private List<OrderItemResponse> findOrderItems(List<Long> orderIds) {
        List<OrderItemResponse> result = orderRepository.findOrderItems(orderIds);
        return result;
    }

    public List<Order> findOrdersV1(OrderSearch orderSearch) {
        return orderRepository.findOrdersV1(orderSearch);
    }

    public List<OrderResponse> findOrdersV2(OrderSearch orderSearch) {
        return orderRepository.findOrdersV2(orderSearch);
    }
}