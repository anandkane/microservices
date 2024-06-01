package org.example.explore.virtualstore.productservice.query.dataaccess.repositories;

import org.example.explore.virtualstore.productservice.query.dataaccess.entities.ProductsLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsLookupRepository extends JpaRepository<ProductsLookupEntity, String> {
    Optional<ProductsLookupEntity> findByTitleOrProductId(String title, String productId);
}
