package org.example.explore.virtualstore.orderservice.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.explore.virtualstore.orderservice.core.constants.OrderStatus;

@Data
@NoArgsConstructor
public class OrderRejectedEvent {
    private String orderId;
    private String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;

    public OrderRejectedEvent(String orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }
}
