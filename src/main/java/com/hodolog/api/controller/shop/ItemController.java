package com.hodolog.api.controller.shop;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.domain.shop.item.Book;
import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.request.shop.ItemBook;
import com.hodolog.api.response.PostResponse;
import com.hodolog.api.response.TagResponse;
import com.hodolog.api.service.PostService;
import com.hodolog.api.service.shop.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/shop/item/book")
    public void post(@RequestBody @Valid ItemBook itemBook) {
        log.info("itemBook: {}", itemBook);
        Book book = Book.builder()
                .name(itemBook.getName())
                .price(itemBook.getPrice())
                .stockQuantity(itemBook.getStockQuantity())
                .author(itemBook.getAuthor())
                .isbn(itemBook.getIsbn())
                .build();
        itemService.saveItem(book);
    }

    @GetMapping("/shop/items")
    public List<Item> getList() {
        List<Item> items = itemService.findItems();
        return items;
    }

    @PatchMapping("/shop/items/book/{itemId}")
    public void edit(@PathVariable Long itemId, @RequestBody @Valid ItemBook itemBook) {
        itemService.editItemBook(itemId, itemBook);
    }
}












