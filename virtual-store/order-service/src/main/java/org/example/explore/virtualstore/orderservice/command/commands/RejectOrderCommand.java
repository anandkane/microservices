package org.example.explore.virtualstore.orderservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
    private String reason;
}
