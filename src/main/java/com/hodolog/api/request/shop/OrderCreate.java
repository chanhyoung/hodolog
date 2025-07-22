package com.hodolog.api.request.shop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderCreate {
  private Long memberId;
  private Long itemId;
  private int count;

}
