package com.hodolog.api.domain.shop.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {
  private String artist;
  private String etc;

  @Builder
  public Album(String name, int price, int stockQuantity, String artist, String etc) {
    super(name, price, stockQuantity);
    this.artist = artist;
    this.etc = etc;
  }
}
