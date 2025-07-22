package com.hodolog.api.service.shop;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.exception.shop.ItemNotFound;
import com.hodolog.api.repository.shop.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("아이템을 찾을 수 없습니다. 아이템 ID: " + itemId));
    }
}
