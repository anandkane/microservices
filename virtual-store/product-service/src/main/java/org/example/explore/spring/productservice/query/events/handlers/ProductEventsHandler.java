package org.example.explore.virtualstore.productservice.query.events.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.example.explore.virtualstore.core.event.product.events.ProductReservationCancelledEvent;
import org.example.explore.virtualstore.core.event.product.events.ProductReservedEvent;
import org.example.explore.virtualstore.productservice.core.dataaccess.entities.ProductEntity;
import org.example.explore.virtualstore.productservice.core.dataaccess.repositories.ProductRepository;
import org.example.explore.virtualstore.productservice.core.events.ProductCreatedEvent;
import org.example.explore.virtualstore.productservice.query.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventsHandler {
    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        log.info("Product created event received: {}", event);
        ProductEntity productEntity = ProductEntity.builder()
                .productId(event.getProductId())
                .title(event.getTitle())
                .price(event.getPrice())
                .quantity(event.getQuantity())
                .build();
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        log.info("Product reserved event received: {}", event);
        ProductEntity productEntity = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(event.getProductId()));
        productEntity.setQuantity(productEntity.getQuantity() - event.getQuantity());
        productRepository.save(productEntity);
        log.info("Product quantity updated: {}", productEntity.getQuantity());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        log.info("Product reservation cancelled event received: {}", event);
        ProductEntity productEntity = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(event.getProductId()));
        productEntity.setQuantity(productEntity.getQuantity() + event.getQuantity());

        log.info("Updating product quantity: {}", productEntity.getQuantity());
        productRepository.save(productEntity);
        log.info("Product quantity updated: {}", productEntity.getQuantity());
    }
}
