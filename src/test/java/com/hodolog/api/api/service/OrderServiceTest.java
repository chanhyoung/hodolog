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
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.item.Book;
import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.exception.shop.NotEnoughStockException;

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
        Member member = createMember("홍길동", "강남구", "역삼동", "123-456");
        Item book = createBook("JPA 시작하기", 10000, 10, "홍길동", "1234567890");


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
        assertEquals("역삼동", getOrder.getDelivery().getAddress().getStreet());
        assertEquals(8, book.getStockQuantity()); // 주문 후 재고 감소 확인
    }

    @Test
    @DisplayName("상품주문 재고수량 초과")
    void test2() {
        // given
        Member member = createMember("홍길동", "강남구", "역삼동", "123-456");
        Item book = createBook("JPA 시작하기", 10000, 10, "홍길동", "1234567890");

        // when & then
        // 주문 수량이 재고 수량을 초과하는 경우 예외 발생
        int orderCount = 11;
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }

    private Item createBook(String name, int price, int stockQuantity, String author, String isbn) {
        Item book = Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author(author)
                .isbn(isbn)
                .build();
        itemRepository.save(book);
        return book;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = Member.builder()
                .name(name)
                .address(new Address(city, street, zipcode))
                .build();
        memberRepository.save(member);
        return member;
    }

    @Test
    @DisplayName("주문 취소")
    void test3() {
        // given
        Member member = createMember("홍길동", "강남구", "역삼동", "123-456");
        Item book = createBook("JPA 시작하기", 10000, 10, "홍길동", "1234567890");

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, book.getStockQuantity()); // 취소 후 재고가 원래대로 돌아옴
    }
}









