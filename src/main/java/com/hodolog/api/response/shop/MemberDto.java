package com.hodolog.api.response.shop;

import com.hodolog.api.domain.shop.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemberDto {
  private String name;
  private Address address;
}
