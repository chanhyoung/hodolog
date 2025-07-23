package com.hodolog.api.service.shop;

import java.util.List;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hodolog.api.domain.shop.Delivery;
import com.hodolog.api.domain.shop.Member;
import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderItem;
import com.hodolog.api.domain.shop.item.Item;
import com.hodolog.api.exception.shop.ItemNotFound;
import com.hodolog.api.repository.shop.ItemRepository;
import com.hodolog.api.repository.shop.MemberRepository;
import com.hodolog.api.repository.shop.OrderRepository;
import com.hodolog.api.request.shop.OrderSearch;
import com.hodolog.api.response.shop.OrderItemResponse;
import com.hodolog.api.response.shop.OrderResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문을 저장하는 메서드
     * @param memberId 회원 ID
     * @param itemId 아이템 ID
     * @param count 주문 수량
     * @return 주문 ID
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ItemNotFound("회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFound("아이템을 찾을 수 없습니다. 아이템 ID: " + itemId));

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 회원의 주소를 배송지로 설정

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);
        
        orderRepository.save(order);
        log.info("주문이 저장되었습니다. 주문 ID: {}", order.getId());
        return order.getId();
    }

    /**
     * 주문 취소 메서드
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFound("주문을 찾을 수 없습니다. 주문 ID: " + orderId));
        
        order.cancel();
        log.info("주문이 취소되었습니다. 주문 ID: {}", orderId);
    }
}
