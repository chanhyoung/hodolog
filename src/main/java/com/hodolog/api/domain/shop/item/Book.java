package com.hodolog.api.domain.shop.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.hodolog.api.request.shop.ItemBook;

import lombok.Builder;
import lombok.Getter;

@Entity
@DiscriminatorValue("B")
@Getter
public class Book extends Item {
  private String author;
  private String isbn;

  @Builder
  public Book(String name, int price, int stockQuantity, String author, String isbn) {
    super(name, price, stockQuantity);
    this.author = author;
    this.isbn = isbn;
  }

  public void edit(ItemBook itemBook) {
    this.setName(itemBook.getName());
    this.setPrice(itemBook.getPrice());
    this.setStockQuantity(itemBook.getStockQuantity());
    this.author = itemBook.getAuthor();
    this.isbn = itemBook.getIsbn();
  }
}
