package com.hodolog.api.api.querydsl;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.QDelivery;
import com.hodolog.api.domain.shop.QMember;
import com.hodolog.api.domain.shop.QOrder;
import com.hodolog.api.domain.shop.QOrderItem;
import com.hodolog.api.domain.shop.item.QItem;
import com.hodolog.api.response.shop.MemberDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
  @Autowired EntityManager em;
  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);
  }

  @Test
  @DisplayName("Querydsl 기본 테스트")
  public void test1() {
    List<Order> orders = queryFactory
            .selectFrom(QOrder.order)
            .join(QOrder.order.member, QMember.member).fetchJoin()
            .join(QOrder.order.delivery, QDelivery.delivery).fetchJoin()
            .where(QOrder.order.member.name.eq("userA"))
            // .limit(1000)
            // .offset(0)
            .fetch();

    assertEquals(1L, orders.size());
    Order getOrder = orders.get(0);
    assertEquals("userA", getOrder.getMember().getName());
  }

  @Test
  @DisplayName("Querydsl 페이징 테스트")
  public void test2() {
    List<Member> result = queryFactory
            .selectFrom(QMember.member)
            .orderBy(QMember.member.name.desc())
            .limit(1)
            .offset(0)
            .fetch();

    assertEquals(1L, result.size());
    assertEquals("userB", result.get(0).getName());

    JPAQuery<Long> countQeury = queryFactory
            .select(QMember.member.count())
            .from(QMember.member);

    assertEquals(2L, countQeury.fetchOne().longValue());
  }

  @Test
  @DisplayName("Querydsl aggregation 테스트 - 구매자별 제품별 수량합계, 주문금액합계")
  public void test3() {
    QOrder order = QOrder.order;
    QMember member = QMember.member;
    QOrderItem orderItem = QOrderItem.orderItem;
    QItem item = QItem.item;

    List<Tuple> fetch = queryFactory
            .select(
              member.id,
              item.name,
              orderItem.orderPrice.sum().as("order_price"),
              orderItem.count.sum().as("count")
            )
            .from(order)
            .join(member).on(order.member.id.eq(member.id))
            .join(orderItem).on(order.id.eq(orderItem.order.id))
            .join(item).on(orderItem.item.id.eq(item.id))
            .where(order.status.eq(OrderStatus.ORDER))
            .groupBy(member.id, item.name)
            .orderBy(member.id.asc(), item.name.asc())
            .fetch();
    
    Tuple item1 = fetch.get(0);
    Tuple item2 = fetch.get(1);
    assertEquals(4, fetch.size());

    assertAll("첫 번째 아이템 검증",
        () -> assertEquals("JPA1 BOOK", item1.get(item.name)),
        () -> assertEquals(10000, item1.get(orderItem.orderPrice.sum().as("order_price"))),
        () -> assertEquals(1, item1.get(orderItem.count.sum().as("count")))
    );
    
    assertAll("두 번째 아이템 검증",
        () -> assertEquals("JPA2 BOOK", item2.get(item.name)),
        () -> assertEquals(20000, item2.get(orderItem.orderPrice.sum().as("order_price"))),
        () -> assertEquals(2, item2.get(orderItem.count.sum().as("count")))
    );

    // 아이템 이름들을 리스트로 추출하여 검증
    List<String> itemNames = fetch.stream()
            .map(tuple -> tuple.get(item.name))
            .collect(Collectors.toList());
    
    // 순서대로 검증
    assertIterableEquals(
        java.util.Arrays.asList("JPA1 BOOK", "JPA2 BOOK", "SPRING1 BOOK", "SPRING2 BOOK"), 
        itemNames
    );

    // 또는 개별적으로 검증
    assertEquals("JPA1 BOOK", itemNames.get(0));
    assertEquals("JPA2 BOOK", itemNames.get(1));
    
    // 특정 아이템이 포함되어 있는지 검증
    assertTrue(itemNames.contains("JPA1 BOOK"));
    assertTrue(itemNames.contains("JPA2 BOOK"));
  }

  @Test
  @DisplayName("subQuery 테스트")
  public void test4() {
    List<Member> result = queryFactory
            .selectFrom(QMember.member)
            .orderBy(QMember.member.name.desc())
            .limit(1)
            .offset(0)
            .fetch();

    assertEquals(1L, result.size());
    assertEquals("userB", result.get(0).getName());

    JPAQuery<Long> countQeury = queryFactory
            .select(QMember.member.count())
            .from(QMember.member);

    assertEquals(2L, countQeury.fetchOne().longValue());
  }

@Test
@DisplayName("최대 주문금액 조회 테스트")
public void testMaxOrderPrice() {
    // Given
    QOrder orders = QOrder.order;
    QOrderItem orderItem = QOrderItem.orderItem;
    QMember member = QMember.member;
    
    QOrder subOrders = new QOrder("subOrders");
    QOrderItem subOrderItem = new QOrderItem("subOrderItem");
    
    // When
    List<Tuple> result = queryFactory
        .select(
            orders.id,
            member.name,
            orders.orderDate,
            orderItem.count.multiply(orderItem.orderPrice).as("total_price")
        )
        .from(orders)
        .join(orderItem).on(orders.id.eq(orderItem.order.id))
        .join(member).on(orders.member.id.eq(member.id))
        .where(
            orders.status.eq(OrderStatus.ORDER)
            .and(
                orderItem.count.multiply(orderItem.orderPrice)
                .eq(
                    JPAExpressions
                        .select(subOrderItem.count.multiply(subOrderItem.orderPrice).max())
                        .from(subOrders)
                        .join(subOrderItem).on(subOrders.id.eq(subOrderItem.order.id))
                        .where(subOrders.status.eq(orders.status))
                )
            )
        )
        .fetch();
    
    // Then
    assertAll("최대 주문금액 검증",
        () -> assertTrue(result.size() > 0),
        () -> {
            Tuple firstResult = result.get(0);
            assertNotNull(firstResult.get(orders.id));
            assertEquals("userB", firstResult.get(member.name));
            assertNotNull(firstResult.get(orders.orderDate));
            assertEquals(160000, firstResult.get(orderItem.count.multiply(orderItem.orderPrice).as("total_price")));
        }
    );
  }

  @Test
  @DisplayName("생성자료 dto 조회 테스트")
  public void findDtoByConstructor() {
    // Given
    List<MemberDto> result = queryFactory
            .select(Projections.constructor(MemberDto.class,
            QMember.member.name, 
            QMember.member.address))
            .from(QMember.member)
            .fetch();

    for (MemberDto memberDto : result) {
        System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  @DisplayName("bulk update 테스트")
  public void bulkUpdate() {
    // Given
    long count = queryFactory
            .update(QMember.member)
            .set(QMember.member.name, "userC")
            .execute();

    em.flush();
    em.clear();
            
    List<Member> result = queryFactory
      .selectFrom(QMember.member)
      .fetch();

    for (Member member : result) {
        System.out.println("member = " + member);
    }
  }

  @Test
  @DisplayName("sql function 테스트")
  public void sqlFunction() {
    List<String> result = queryFactory
      .select(Expressions.stringTemplate(
        "upper({0})", 
        QMember.member.name))
      .from(QMember.member)
      .fetch();

    for (String str : result) {
        System.out.println("str = " + str);
    }
  }
}
