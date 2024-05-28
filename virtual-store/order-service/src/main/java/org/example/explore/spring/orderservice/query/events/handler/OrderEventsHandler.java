package org.example.explore.virtualstore.orderservice.query.events.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.example.explore.virtualstore.orderservice.core.events.OrderApprovedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderCreatedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderRejectedEvent;
import org.example.explore.virtualstore.orderservice.core.exceptions.OrderNotFoundException;
import org.example.explore.virtualstore.orderservice.query.dataaccess.entity.OrderEntity;
import org.example.explore.virtualstore.orderservice.query.dataaccess.repository.OrdersRepository;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
@AllArgsConstructor
@Slf4j
public class OrderEventsHandler {
    private final OrdersRepository ordersRepository;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent: {}", event);
        OrderEntity orderEntity = OrderEntity.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .addressId(event.getAddressId())
                .orderStatus(event.getOrderStatus())
                .build();
        log.info("Saving order: {}", orderEntity);
        ordersRepository.save(orderEntity);
        log.info("Order saved: {}", orderEntity);
//        throw new RuntimeException("Test exception 1");
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        log.info("Handling OrderApprovedEvent: {}", event);
        OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));
        orderEntity.setOrderStatus(event.getOrderStatus());

        log.info("Updating order: {}", orderEntity);
        ordersRepository.save(orderEntity);
        log.info("Order updated: {}", orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        log.info("Handling OrderRejectedEvent: {}", event);
        OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));
        orderEntity.setOrderStatus(event.getOrderStatus());
        orderEntity.setMessage(event.getReason());

        log.info("Updating order status: {}", orderEntity.getOrderStatus());
        ordersRepository.save(orderEntity);
        log.info("Order status updated: {}", orderEntity.getOrderStatus());
    }
}
