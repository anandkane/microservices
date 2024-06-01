package org.example.explore.virtualstore.orderservice.command.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderCommand {
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
}
