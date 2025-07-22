package com.hodolog.api.service.shop;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.shop.item.Book;
import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.exception.shop.ItemNotFound;
import com.hodolog.api.repository.shop.ItemRepository;
import com.hodolog.api.request.shop.ItemBook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("아이템을 찾을 수 없습니다. 아이템 ID: " + itemId));
    }

    @Transactional
    public void editItemBook(Long itemId, ItemBook itemBook) {
        Book book = (Book)itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("아이템을 찾을 수 없습니다. 아이템 ID: " + itemId));

        book.edit(itemBook);
    }
}
