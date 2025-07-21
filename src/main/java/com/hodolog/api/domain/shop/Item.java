package com.hodolog.api.domain.shop;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
	private Long id;

  private String name;

  private int price;

  private int stockQuantity;
}
