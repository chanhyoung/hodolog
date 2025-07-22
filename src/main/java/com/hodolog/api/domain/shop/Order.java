package com.hodolog.api.domain.shop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.hodolog.api.exception.shop.IllegalStateException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Getter
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
	private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate;  // 주문시간

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문상태 [ORDER, CANCEL]

  //= 연관관계 메서드 ==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  @Builder
  public Order(Member member, Delivery delivery, OrderItem... orderItems) {
    this.member = member;
    this.delivery = delivery;
    this.orderDate = LocalDateTime.now();
    this.status = OrderStatus.ORDER;

    for (OrderItem orderItem : orderItems) {
      addOrderItem(orderItem);
    }
  }

  //== 생성 메서드 ==//
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = Order.builder()
        .member(member)
        .delivery(delivery)
        .orderItems(orderItems)
        .build();

    return order;
  }

  //== 주문 취소 ==//
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }

    this.status = OrderStatus.CANCEL;
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  //== 조회 로직 ==//
  /**
   * 전체 주문 가격 조회
   */
  public int getTotalPrice() {
    return orderItems.stream()
        .mapToInt(orderItem -> orderItem.getTotalPrice())
        .sum();
  }
}
