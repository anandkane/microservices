package org.example.explore.virtualstore.productservice.core.services;

import org.example.explore.virtualstore.productservice.core.dataaccess.entities.ProductEntity;
import org.example.explore.virtualstore.productservice.core.dataaccess.repositories.ProductRepository;
import org.example.explore.virtualstore.productservice.core.rest.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        ProductEntity productEntity = convertToEntity(product);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        return convertToModel(savedProductEntity);
    }

    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Product getProductById(String id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        return productEntity != null ? convertToModel(productEntity) : null;
    }

    public Product updateProduct(Product product) {
        ProductEntity productEntity = convertToEntity(product);
        ProductEntity updatedProductEntity = productRepository.save(productEntity);
        return convertToModel(updatedProductEntity);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    private Product convertToModel(ProductEntity productEntity) {
        return Product.builder()
                .productId(productEntity.getProductId())
                .title(productEntity.getTitle())
                .price(productEntity.getPrice())
                .quantity(productEntity.getQuantity())
                .build();
    }

    private ProductEntity convertToEntity(Product product) {
        return ProductEntity.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}