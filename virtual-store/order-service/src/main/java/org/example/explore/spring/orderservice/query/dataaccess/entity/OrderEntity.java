package org.example.explore.virtualstore.orderservice.query.dataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.explore.virtualstore.orderservice.core.constants.OrderStatus;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    private String orderId;
    @NotNull
    private String userId;
    @NotNull
    private String productId;
    @NotNull
    private int quantity;
    @NotNull
    private String addressId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String message;
}
