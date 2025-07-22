package com.hodolog.api.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hodolog.api.domain.shop.item.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
