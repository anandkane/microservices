package org.example.explore.virtualstore.orderservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveOrderCommand {
    @TargetAggregateIdentifier
    private String orderId;
}
