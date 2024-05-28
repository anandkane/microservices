package org.example.explore.virtualstore.paymentservice.query.events.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.example.explore.virtualstore.core.event.payment.events.PaymentProcessedEvent;
import org.example.explore.virtualstore.core.event.payment.events.PaymentReversedEvent;
import org.example.explore.virtualstore.paymentservice.query.dataccess.entity.PaymentEntity;
import org.example.explore.virtualstore.paymentservice.query.dataccess.repository.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("payment-group")
@Slf4j
@AllArgsConstructor
public class PaymentServiceEventsHandler {

    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        log.info("Payment processed event received: {}", paymentProcessedEvent);
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentId(paymentProcessedEvent.getPaymentId())
                .orderId(paymentProcessedEvent.getOrderId())
                .build();

        log.info("Saving payment entity: {}", paymentEntity);
        paymentRepository.save(paymentEntity);
        log.info("Payment entity saved: {}", paymentEntity);
    }

    @EventHandler
    public void on(PaymentReversedEvent paymentReversedEvent) {
        log.info("Payment reversed event received: {}", paymentReversedEvent);
        paymentRepository.findByPaymentId(paymentReversedEvent.getPaymentId())
                .ifPresent(paymentEntity -> {
                    log.info("Deleting payment entity: {}", paymentEntity);
                    paymentRepository.delete(paymentEntity);
                    log.info("Payment entity deleted: {}", paymentEntity);
                });
    }
}
