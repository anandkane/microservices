package org.example.explore.virtualstore.orderservice.core.rest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.explore.virtualstore.orderservice.core.constants.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
