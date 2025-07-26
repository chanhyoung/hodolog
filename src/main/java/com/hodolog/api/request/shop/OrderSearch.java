package com.hodolog.api.request.shop;

import com.hodolog.api.domain.shop.OrderStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
@ToString
public class OrderSearch {
  private String memberName;
  private OrderStatus orderStatus; // 주문 상태 (예: ORDER, CANCEL 등)
  private OrderSearchSortType sortType; // 정렬 기준

  private static final int MAX_SIZE = 2000;

  @Builder.Default
  private Integer page = 1;

  @Builder.Default
  private Integer limit = 10;

  public long getOffset() {
      return (long) (max(1, page) - 1) * min(limit, MAX_SIZE);
  }
}
