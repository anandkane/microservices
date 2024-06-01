package org.example.explore.virtualstore.productservice.command.aggregates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.explore.virtualstore.core.command.product.commands.CancelProductReservationCommand;
import org.example.explore.virtualstore.core.command.product.commands.ReserveProductCommand;
import org.example.explore.virtualstore.core.event.product.events.ProductReservationCancelledEvent;
import org.example.explore.virtualstore.core.event.product.events.ProductReservedEvent;
import org.example.explore.virtualstore.productservice.command.commands.CreateProductCommand;
import org.example.explore.virtualstore.productservice.core.events.ProductCreatedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Builder
@Data
@AllArgsConstructor
@Slf4j
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private Double price;
    private int quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        log.info("Command received: {}", command);
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(command.getProductId())
                .title(command.getTitle())
                .price(command.getPrice())
                .quantity(command.getQuantity())
                .build();
        log.info("Applying event: {}", productCreatedEvent);
        apply(productCreatedEvent);
        log.info("Event applied: {}", productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        log.info("Event received: {}", event);
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
    }

    @CommandHandler
    public void handle(ReserveProductCommand command) {
        log.info("Command received: {}", command);
        if (this.quantity < command.getQuantity()) {
            log.error("Failed to reserve product due to insufficient quantity");
            throw new IllegalArgumentException("Insufficient quantity");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .userId(command.getUserId())
                .orderId(command.getOrderId())
                .build();
        log.info("Applying event: {}", productReservedEvent);
        apply(productReservedEvent);
        log.info("Event applied: {}", productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        log.info("Event received: {}", event);
        this.quantity -= event.getQuantity();
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command) {
        log.info("Command received: {}", command);

        ProductReservationCancelledEvent productReservationCancelledEvent = new ProductReservationCancelledEvent(
                command.getProductId(), command.getOrderId(), command.getQuantity(), command.getMessage());
        log.info("Applying event: {}", productReservationCancelledEvent);
        apply(productReservationCancelledEvent);
        log.info("Event applied: {}", productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent event) {
        log.info("Event received: {}", event);
        this.quantity += event.getQuantity();
    }
}

