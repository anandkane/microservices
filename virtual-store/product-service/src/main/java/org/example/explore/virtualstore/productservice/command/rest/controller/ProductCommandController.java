package org.example.explore.virtualstore.productservice.command.rest.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.example.explore.virtualstore.productservice.command.commands.CreateProductCommand;
import org.example.explore.virtualstore.productservice.core.rest.models.Product;
import org.example.explore.virtualstore.productservice.core.services.ProductService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductCommandController {

    private final Environment environment;
    private final CommandGateway commandGateway;
    private final ProductService productService;

    public ProductCommandController(Environment environment, CommandGateway commandGateway, ProductService productService) {
        this.environment = environment;
        this.commandGateway = commandGateway;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
        log.info("Seeding command: {}", createProductCommand);
        Object commandResponse = commandGateway.sendAndWait(createProductCommand);
        log.info("Command response: {}", commandResponse);

        return new ResponseEntity<>(commandResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        product.setProductId(id);
        Product updatedProduct = productService.updateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/status")
    public String status() {
        return "Product Service is running on port: " + environment.getProperty("local.server.port");
    }
}