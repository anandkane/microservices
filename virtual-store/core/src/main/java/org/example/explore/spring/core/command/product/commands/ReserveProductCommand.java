package org.example.explore.virtualstore.core.command.product.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ReserveProductCommand {
    @TargetAggregateIdentifier
    private String productId;
    private int quantity;
    private String userId;
    private String orderId;
}
