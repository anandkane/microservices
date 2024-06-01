package org.example.explore.virtualstore.productservice.core.dataaccess.repositories;

import org.example.explore.virtualstore.productservice.core.dataaccess.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findByProductIdOrTitle(String productId, String title);
    Optional<ProductEntity> findByTitle(String title);
}
