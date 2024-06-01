package org.example.explore.virtualstore.productservice.query.events.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.example.explore.virtualstore.productservice.core.events.ProductCreatedEvent;
import org.example.explore.virtualstore.productservice.query.dataaccess.entities.ProductsLookupEntity;
import org.example.explore.virtualstore.productservice.query.dataaccess.repositories.ProductsLookupRepository;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@AllArgsConstructor
@Slf4j
public class ProductLookupEventHandler {
    private final ProductsLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        log.info("Product created event received: {}", event);
        productLookupRepository.save(new ProductsLookupEntity(event.getProductId(), event.getTitle()));
//        throw new RuntimeException("Test exception");
    }
}
