package com.hodolog.api.domain.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.exception.shop.IllegalArgumentException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
	private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="item_id")
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="order_id")
  private Order order;

  private int orderPrice;  // 주문가격
  private int count;  // 주문수량

  public void setOrder(Order order) {
    this.order = order;
  }

  @Builder
  public OrderItem(Item item, int orderPrice, int count) {
    this.item = item;
    this.orderPrice = orderPrice;
    this.count = count;

    // 주문 수량이 음수인 경우 예외 처리
    if (count < 0) {
      throw new IllegalArgumentException("주문 수량은 0 이상이어야 합니다.");
    }

    // 주문 가격이 음수인 경우 예외 처리
    if (orderPrice < 0) {
      throw new IllegalArgumentException("주문 가격은 0 이상이어야 합니다.");
    }
  }

  //== 생성 메서드 ==//
  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    // 주문 아이템 생성
    OrderItem orderItem = OrderItem.builder()
        .item(item)
        .orderPrice(orderPrice)
        .count(count)
        .build();
        
    // 아이템 재고 감소
    item.removeStock(count);
    return orderItem;
  }
 
  //== 비지니스 로직 ==//
  public void cancel() {
    item.addStock(count);
  }

  //== 조회 로직 ==//
  /**
   * 주문 상품 전체 가격 조회
   */
  public int getTotalPrice() {
    return orderPrice * count;
  }
}
