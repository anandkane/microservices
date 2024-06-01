package org.example.explore.virtualstore.orderservice.command.aggregates;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.explore.virtualstore.orderservice.command.commands.ApproveOrderCommand;
import org.example.explore.virtualstore.orderservice.command.commands.CreateOrderCommand;
import org.example.explore.virtualstore.orderservice.command.commands.RejectOrderCommand;
import org.example.explore.virtualstore.orderservice.core.constants.OrderStatus;
import org.example.explore.virtualstore.orderservice.core.events.OrderApprovedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderCreatedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderRejectedEvent;

import static org.example.explore.virtualstore.orderservice.core.constants.GenericConstants.USER_ID;

@Aggregate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class OrderAggregate {
    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        log.info("Create order command received: {}", createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(createOrderCommand.getOrderId())
                .userId(USER_ID)
                .productId(createOrderCommand.getProductId())
                .quantity(createOrderCommand.getQuantity())
                .addressId(createOrderCommand.getAddressId())
                .build();

        log.info("Applying order created event: {}", orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
        log.info("Order created event applied: {}", orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        log.info("Order created event received: {}", orderCreatedEvent);
        this.orderId = orderCreatedEvent.getOrderId();
        this.userId = orderCreatedEvent.getUserId();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.addressId = orderCreatedEvent.getAddressId();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand) {
        log.info("Approve order command received: {}", approveOrderCommand);

        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());

        log.info("Applying order approved event: {}", orderApprovedEvent);
        AggregateLifecycle.apply(orderApprovedEvent);
        log.info("Order approved event applied: {}", orderApprovedEvent);

        // For testing purpose
        throw new RuntimeException("Order cannot be approved");
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order approved event received: {}", orderApprovedEvent);
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
        log.info("Reject order command received: {}", rejectOrderCommand);

        OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(),
                rejectOrderCommand.getReason());

        log.info("Applying order rejected event: {}", orderRejectedEvent);
        AggregateLifecycle.apply(orderRejectedEvent);
        log.info("Order rejected event applied: {}", orderRejectedEvent);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent) {
        log.info("Order rejected event received: {}", orderRejectedEvent);
        this.orderStatus = orderRejectedEvent.getOrderStatus();
    }
}
