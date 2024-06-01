package org.example.explore.virtualstore.productservice.core.dataaccess.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String productId;
    @Column(unique = true, nullable = false)
    private String title;
    private Double price;
    private int quantity;
}