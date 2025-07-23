package com.hodolog.api.response.shop;

import java.time.LocalDateTime;

import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String memberName;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Address address;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.memberName = order.getMember().getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus().name();
        this.address = order.getDelivery().getAddress();
    }

    // @QueryProjection을 사용한 생성자 (컴파일 타임에 체크 가능)
    @QueryProjection
    public OrderResponse(Long id, String memberName, LocalDateTime orderDate, 
                        OrderStatus orderStatus, Address address) {
        this.id = id;
        this.memberName = memberName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus.name();
        this.address = address;
    }
}