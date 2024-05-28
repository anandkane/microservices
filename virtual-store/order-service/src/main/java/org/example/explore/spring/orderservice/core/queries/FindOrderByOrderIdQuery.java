package org.example.explore.virtualstore.orderservice.core.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindOrderByOrderIdQuery {
    private String orderId;
}