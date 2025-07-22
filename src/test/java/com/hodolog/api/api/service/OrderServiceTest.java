package com.hodolog.api.api.service;

import com.hodolog.api.repository.shop.ItemRepository;
import com.hodolog.api.repository.shop.MemberRepository;
import com.hodolog.api.repository.shop.OrderRepository;
import com.hodolog.api.service.shop.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import com.hodolog.api.domain.Post;
import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.item.Book;
import com.hodolog.api.domain.shop.item.Item;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;
    
    @BeforeEach
    void clean() {
        orderRepository.deleteAll();
    }

    // 주문저장 테스트
    @Test
    @DisplayName("상품 주문")
    void test1() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .address(new Address("서울시", "강남구", "123-456"))
                .build();
        memberRepository.save(member);
        Item book = Book.builder()
                .name("JPA 책")
                .price(10000)
                .stockQuantity(10)
                .author("홍길동")
                .isbn("1234567890")
                .build();
        itemRepository.save(book);

        // when
        int orderCount = 2;
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        // 주문이 정상적으로 저장되었는지 확인
        assertEquals(1L, orderRepository.count());
        Order getOrder = orderRepository.findAll().get(0);
        assertEquals(member.getId(), getOrder.getMember().getId());
        assertEquals(book.getId(), getOrder.getOrderItems().get(0).getItem().getId());
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(orderCount, getOrder.getOrderItems().get(0).getCount());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("서울시 강남구 123-456", getOrder.getDelivery().getAddress().toString());
        assertEquals(8, book.getStockQuantity()); // 주문 후 재고 감소 확인
    }
}









