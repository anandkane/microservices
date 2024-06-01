package org.example.explore.virtualstore.productservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public final class FindProductByProductIdQuery {
    private final String productId;
}
