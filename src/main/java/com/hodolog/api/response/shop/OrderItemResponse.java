package com.hodolog.api.response.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hodolog.api.domain.shop.OrderItem;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class OrderItemResponse {
    @JsonIgnore
    private Long orderId;  // 주문 아이템 ID
    private String itemName; // 상품명
    private int orderPrice;  // 주문 가격
    private int count;  // 주문수량

    public OrderItemResponse(OrderItem orderItem) {
        this.orderId = orderItem.getOrder().getId();  // 주문 아이템 ID
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();        
    }

    // @QueryProjection을 사용한 생성자 (컴파일 타임에 체크 가능)
    @QueryProjection
    public OrderItemResponse(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }    
}