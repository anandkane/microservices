package org.example.explore.virtualstore.productservice.core.rest.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @Min(value = 0, message = "Price should not be less than 0")
    private Double price;

    @Min(value = 1, message = "Quantity should not be less than 1")
    @Max(value = 1000, message = "Quantity should not be greater than 1000")
    private int quantity;
}