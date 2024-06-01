package org.example.explore.virtualstore.productservice.query.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.example.explore.virtualstore.productservice.core.rest.models.Product;
import org.example.explore.virtualstore.productservice.query.queries.FindProductByProductIdQuery;
import org.example.explore.virtualstore.productservice.query.queries.FindProductsQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductQueryController {

    private final QueryGateway queryGateway;

    public ProductQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        FindProductsQuery productsQuery = new FindProductsQuery();
        List<Product> products = queryGateway.query(productsQuery,
                                                    ResponseTypes.multipleInstancesOf(Product.class)).join();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        FindProductByProductIdQuery productQuery = new FindProductByProductIdQuery(id);
        Product product = queryGateway.query(productQuery, Product.class).join();
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}