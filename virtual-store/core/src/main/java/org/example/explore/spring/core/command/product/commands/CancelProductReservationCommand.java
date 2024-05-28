package org.example.explore.virtualstore.core.command.product.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelProductReservationCommand {
    @TargetAggregateIdentifier
    private String productId;
    private int quantity;
    private String userId;
    private String orderId;
    private String message;
}
