package com.hodolog.api.domain.shop.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.hodolog.api.domain.shop.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Movie extends Item {
  private String director;
  private String actor;
}
