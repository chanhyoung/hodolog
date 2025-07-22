package com.hodolog.api.request.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ItemBook {
  private String name;
  private int price;
  private int stockQuantity;
  private String author;
  private String isbn;

  @Builder
  public ItemBook(String name, int price, int stockQuantity, String author, String isbn) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.author = author;
    this.isbn = isbn;
  }
}
