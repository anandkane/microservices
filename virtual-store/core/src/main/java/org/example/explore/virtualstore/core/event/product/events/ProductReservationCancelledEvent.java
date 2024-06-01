package org.example.explore.virtualstore.core.event.product.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationCancelledEvent {
    private String productId;
    private String orderId;
    private int quantity;
    private String message;
}
