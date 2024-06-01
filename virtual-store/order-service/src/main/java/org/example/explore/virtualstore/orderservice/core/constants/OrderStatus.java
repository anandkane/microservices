package org.example.explore.virtualstore.orderservice.core.constants;

public enum OrderStatus {
    CREATED("CREATED"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED"),
    RETURNED("RETURNED"),
    REFUNDED("REFUNDED"),
    COMPLETED("COMPLETED");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
