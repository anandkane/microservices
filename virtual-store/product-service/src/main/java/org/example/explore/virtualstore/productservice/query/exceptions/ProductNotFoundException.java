package org.example.explore.virtualstore.productservice.query.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private final String productId;

    public ProductNotFoundException(String productId) {
        this.productId = productId;
    }

    @Override
    public String getMessage() {
        return "Product not found: " + productId;
    }
}
