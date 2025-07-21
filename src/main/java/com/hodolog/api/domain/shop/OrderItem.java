package com.hodolog.api.domain.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
	private Long id;

  @ManyToOne
  @JoinColumn(name ="item_id")
  private Item item;

  @ManyToOne
  @JoinColumn(name ="order_id")
  private Order order;

  private int orderPrice;  // 주문가격
  private int count;  // 주문수량
}
