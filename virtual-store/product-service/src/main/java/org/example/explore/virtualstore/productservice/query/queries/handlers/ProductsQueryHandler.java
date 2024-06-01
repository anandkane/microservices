package org.example.explore.virtualstore.productservice.query.queries.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.example.explore.virtualstore.productservice.core.dataaccess.entities.ProductEntity;
import org.example.explore.virtualstore.productservice.core.dataaccess.repositories.ProductRepository;
import org.example.explore.virtualstore.productservice.core.rest.models.Product;
import org.example.explore.virtualstore.productservice.query.queries.FindProductByProductIdQuery;
import org.example.explore.virtualstore.productservice.query.queries.FindProductsQuery;
import org.example.explore.virtualstore.productservice.query.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ProductsQueryHandler {
    private final ProductRepository productRepository;

    public ProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<Product> on(FindProductsQuery query) {
        log.info("Handling FindProductsQuery: {}", query);
        List<ProductEntity> productEntities = productRepository.findAll();

        return productEntities.stream()
                .map(productEntity -> Product.builder()
                        .productId(productEntity.getProductId())
                        .title(productEntity.getTitle())
                        .price(productEntity.getPrice())
                        .quantity(productEntity.getQuantity())
                        .build())
                .toList();
    }

    @QueryHandler
    public Product on(FindProductByProductIdQuery query) {
        log.info("Handling FindProductQuery: {}", query);
        Optional<ProductEntity> productEntity = productRepository.findById(query.getProductId());
        return productEntity.map(entity -> Product.builder()
                        .productId(entity.getProductId())
                        .title(entity.getTitle())
                        .price(entity.getPrice())
                        .quantity(entity.getQuantity())
                        .build())
                .orElseThrow(() -> {
                    log.error("Product not found: {}", query.getProductId());
                    return new ProductNotFoundException(query.getProductId());
                });
    }
}
