package org.example.explore.virtualstore.paymentservice.command.aggregates;

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
import org.example.explore.virtualstore.core.command.payment.commands.ProcessPaymentCommand;
import org.example.explore.virtualstore.core.command.payment.commands.ReversePaymentCommand;
import org.example.explore.virtualstore.core.event.payment.events.PaymentProcessedEvent;
import org.example.explore.virtualstore.core.event.payment.events.PaymentReversedEvent;

@Aggregate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
        log.info("Process payment command received: {}", processPaymentCommand);

        if (processPaymentCommand.getPaymentDetails().getCardNumber() == null) {
            log.error("Card number is required");
            throw new IllegalArgumentException("Card number is required");
        }

        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .paymentId(processPaymentCommand.getPaymentId()).orderId(processPaymentCommand.getOrderId()).build();

        log.info("Applying payment processed event: {}", paymentProcessedEvent);
        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("Payment processed event applied: {}", paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("Payment processed event received: {}", paymentProcessedEvent);
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }

    @CommandHandler
    public void handle(ReversePaymentCommand reversePaymentCommand) {
        log.info("Reverse payment command received: {}", reversePaymentCommand);
        PaymentReversedEvent paymentReversedEvent = PaymentReversedEvent.builder()
                .paymentId(reversePaymentCommand.getPaymentId()).orderId(reversePaymentCommand.getOrderId())
                .message(reversePaymentCommand.getMessage()).build();
        log.info("Applying payment reversed event: {}", paymentReversedEvent);
        AggregateLifecycle.apply(paymentReversedEvent);
        log.info("Payment reversed event applied: {}", paymentReversedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentReversedEvent paymentReversedEvent) {
        log.info("Payment reversed event received: {}", paymentReversedEvent);
        this.paymentId = paymentReversedEvent.getPaymentId();
        this.orderId = paymentReversedEvent.getOrderId();
    }
}
