package com.hodolog.api.api.querydsl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hodolog.api.domain.shop.Address;
import com.hodolog.api.domain.shop.Delivery;
import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderItem;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.QDelivery;
import com.hodolog.api.domain.shop.QMember;
import com.hodolog.api.domain.shop.QOrder;
import com.hodolog.api.domain.shop.item.Book;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
  @Autowired EntityManager em;
  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);
    insertOrder1();
    insertOrder2();
  }

  private void insertOrder1() {
    System.out.println("Init1" + this.getClass());
    Member member = createMember("userA", "서울", "1", "1111");
    em.persist(member);

    Book book1 = createBook("JPA1 BOOK", 10000, 100);
    em.persist(book1);

    Book book2 = createBook("JPA2 BOOK", 20000, 100);
    em.persist(book2);

    OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
    OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

    Delivery delivery = createDelivery(member);
    Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
    em.persist(order);
  }

  private void insertOrder2() {
    Member member = createMember("userB", "진주", "2", "2222");
    em.persist(member);

    Book book1 = createBook("SPRING1 BOOK", 20000, 200);
    em.persist(book1);

    Book book2 = createBook("SPRING2 BOOK", 40000, 300);
    em.persist(book2);

    OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
    OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

    Delivery delivery = createDelivery(member);
    Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
    em.persist(order);
  }

  private Member createMember(String name, String city, String street, String zipcode) {
    Member member = new Member();
    member.setName(name);
    member.setAddress(new Address(city, street, zipcode));
    return member;
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = Book.builder()
            .name(name)
            .price(price)
            .stockQuantity(stockQuantity)
            .build();
        
    return book;
  }

  private Delivery createDelivery(Member member) {
    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());
    return delivery;
  }

  @Test
  public void testQuerydsl() {
    List<Order> orders = queryFactory
            .selectFrom(QOrder.order)
            .join(QOrder.order.member, QMember.member).fetchJoin()
            .join(QOrder.order.delivery, QDelivery.delivery).fetchJoin()
            .where(QOrder.order.member.name.eq("userA"))
            // .limit(1000)
            .fetch();

    assertEquals(2L, orders.size());
    Order getOrder = orders.get(0);
    assertEquals("userA", getOrder.getMember().getName());
  }
}
