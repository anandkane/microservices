package org.example.explore.virtualstore.productservice.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreatedEvent {
    private String productId;
    private String title;
    private Double price;
    private int quantity;
}
