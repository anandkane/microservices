package org.example.explore.virtualstore.orderservice.core.events;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.explore.virtualstore.orderservice.core.constants.OrderStatus;

@Data
@NoArgsConstructor
public class OrderApprovedEvent {
    private String orderId;
    private OrderStatus orderStatus = OrderStatus.APPROVED;

    public OrderApprovedEvent(String orderId) {
        this.orderId = orderId;
    }
}
