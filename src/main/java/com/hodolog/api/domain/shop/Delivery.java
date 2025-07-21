package com.hodolog.api.domain.shop;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;

@Entity
@Getter
public class Delivery {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_id")
	private Long id;

  @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
  private Order order;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;  // 배송상태 [READY, COMP]

  public void setOrder(Order order) {
    this.order = order;
  }
}
