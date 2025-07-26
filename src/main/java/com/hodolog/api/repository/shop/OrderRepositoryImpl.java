package com.hodolog.api.repository.shop;

import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.QDelivery;
import com.hodolog.api.domain.shop.QMember;
import com.hodolog.api.domain.shop.QOrder;
import com.hodolog.api.domain.shop.QOrderItem;
import com.hodolog.api.request.shop.OrderSearch;
import com.hodolog.api.request.shop.OrderSearchSortType;
import com.hodolog.api.response.shop.OrderItemResponse;
import com.hodolog.api.response.shop.QOrderItemResponse;
import com.hodolog.api.response.shop.OrderResponse;
import com.hodolog.api.response.shop.QOrderResponse;
import com.hodolog.api.domain.shop.item.QItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> findOrdersV1(OrderSearch orderSearch) {
        return jpaQueryFactory
            .selectFrom(QOrder.order)
            .join(QOrder.order.member, QMember.member).fetchJoin()
            .join(QOrder.order.delivery, QDelivery.delivery).fetchJoin()
            .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
            // .limit(1000)
            .limit(orderSearch.getLimit())
            .offset(orderSearch.getOffset())
            .orderBy(createSpecifier(orderSearch.getSortType()))
            .fetch();
    }

    @Override
    public List<OrderResponse> findOrdersV2(OrderSearch orderSearch) {
        return jpaQueryFactory
            .select(new QOrderResponse(
                QOrder.order.id,
                QOrder.order.member.name,
                QOrder.order.orderDate,
                QOrder.order.status,
                QOrder.order.delivery.address
            ))
            .from(QOrder.order)
            .join(QOrder.order.member, QMember.member)
            .join(QOrder.order.delivery, QDelivery.delivery)
            .where(
                statusEq(orderSearch.getOrderStatus()), 
                nameLike(orderSearch.getMemberName())
            )
            .orderBy(createSpecifier(orderSearch.getSortType()))
            .limit(orderSearch.getLimit())
            .offset(orderSearch.getOffset())
            .fetch();
    }

    private OrderSpecifier<?> createSpecifier(OrderSearchSortType sortType) {
        if (sortType == null) {
            return QOrder.order.orderDate.desc(); // 기본 정렬 기준
        } else {
            if (sortType.equals(OrderSearchSortType.ORDERDATE_ASC)) {
                return QOrder.order.orderDate.asc();
            } else {
                return QOrder.order.orderDate.desc();
            }
        } 
    }

    @Override
    public Page<OrderResponse> findOrdersPaging(OrderSearch orderSearch) {
        List<OrderResponse> content = jpaQueryFactory
            .select(new QOrderResponse(
                QOrder.order.id,
                QOrder.order.member.name,
                QOrder.order.orderDate,
                QOrder.order.status,
                QOrder.order.delivery.address
            ))
            .from(QOrder.order)
            .join(QOrder.order.member, QMember.member)
            .join(QOrder.order.delivery, QDelivery.delivery)
            .where(
                statusEq(orderSearch.getOrderStatus()), 
                nameLike(orderSearch.getMemberName())
            )
            .orderBy(createSpecifier(orderSearch.getSortType()))
            .limit(orderSearch.getLimit())
            .offset(orderSearch.getOffset())
            .fetch();

        long total = jpaQueryFactory
            .select(QOrder.order.count())
            .from(QOrder.order)
            .join(QOrder.order.member, QMember.member)
            .where(
                statusEq(orderSearch.getOrderStatus()), 
                nameLike(orderSearch.getMemberName())
            )
            .fetchOne();
        
        PageRequest pageRequest = PageRequest.of(orderSearch.getPage() - 1, orderSearch.getLimit());
        return new PageImpl<>(content, pageRequest, total);
    }


    @Override
    public List<OrderItemResponse> findOrderItems(List<Long> orderIds) {
        return jpaQueryFactory
            .select(new QOrderItemResponse(
                QOrderItem.orderItem.order.id,
                QOrderItem.orderItem.item.name,
                QOrderItem.orderItem.orderPrice,
                QOrderItem.orderItem.count
            ))
            .from(QOrderItem.orderItem)
            .join(QOrderItem.orderItem.item, QItem.item)
            .where(QOrderItem.orderItem.order.id.in(orderIds))
            .fetch();
    }
    
    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return QMember.member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }
}
