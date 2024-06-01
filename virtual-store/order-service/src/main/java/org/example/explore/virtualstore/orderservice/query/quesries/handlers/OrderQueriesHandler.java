package org.example.explore.virtualstore.orderservice.query.quesries.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.example.explore.virtualstore.orderservice.core.exceptions.OrderNotFoundException;
import org.example.explore.virtualstore.orderservice.core.queries.FindOrderByOrderIdQuery;
import org.example.explore.virtualstore.orderservice.core.rest.models.Order;
import org.example.explore.virtualstore.orderservice.query.dataaccess.entity.OrderEntity;
import org.example.explore.virtualstore.orderservice.query.dataaccess.repository.OrdersRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderQueriesHandler {
    private OrdersRepository ordersRepository;

    @QueryHandler
    public Order handle(FindOrderByOrderIdQuery query) {
        log.info("Handling FindOrderByOrderIdQuery: {}", query);
        OrderEntity orderEntity = ordersRepository.findById(query.getOrderId())
                .orElseThrow(() -> {
                    log.error("Order not found: {}", query.getOrderId());
                    return new OrderNotFoundException(query.getOrderId());
                });

        log.info("Order found: {}", orderEntity);
        return Order.builder()
                .orderId(orderEntity.getOrderId())
                .userId(orderEntity.getUserId())
                .productId(orderEntity.getProductId())
                .quantity(orderEntity.getQuantity())
                .addressId(orderEntity.getAddressId())
                .orderStatus(orderEntity.getOrderStatus())
                .build();
    }
}
