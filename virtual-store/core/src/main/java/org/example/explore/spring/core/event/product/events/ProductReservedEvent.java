package org.example.explore.virtualstore.core.event.product.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductReservedEvent {
    private String productId;
    private int quantity;
    private String userId;
    private String orderId;
}
