package org.example.explore.virtualstore.productservice.query.dataaccess.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products_lookup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsLookupEntity {
    @Id
    private String productId;
    @Column(nullable = false, unique = true)
    private String title;
}
