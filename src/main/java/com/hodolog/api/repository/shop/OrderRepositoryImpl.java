package com.hodolog.api.repository.shop;

import com.hodolog.api.domain.shop.Order;
import com.hodolog.api.domain.shop.OrderStatus;
import com.hodolog.api.domain.shop.QMember;
import com.hodolog.api.domain.shop.QOrder;
import com.hodolog.api.request.shop.OrderSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> findOrders(OrderSearch orderSearch) {
        return jpaQueryFactory
            .selectFrom(QOrder.order)
            .join(QOrder.order.member, QMember.member)
            .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
            .limit(1000)
                // .limit(postSearch.getSize())
                // .offset(postSearch.getOffset())
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
