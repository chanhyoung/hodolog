package com.hodolog.api.domain.shop.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.hodolog.api.domain.shop.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item {
  private String artist;
  private String etc;
}
