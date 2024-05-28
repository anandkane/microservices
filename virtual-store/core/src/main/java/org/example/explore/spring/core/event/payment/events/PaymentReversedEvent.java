package org.example.explore.virtualstore.core.event.payment.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentReversedEvent {
    private String paymentId;
    private String orderId;
    private String message;
}
