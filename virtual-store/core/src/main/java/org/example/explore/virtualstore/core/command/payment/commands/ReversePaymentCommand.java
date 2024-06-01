package org.example.explore.virtualstore.core.command.payment.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.explore.virtualstore.core.dto.PaymentDetails;

@Data
@Builder
public class ReversePaymentCommand {
    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private String message;
    private PaymentDetails paymentDetails;
}
