package org.example.explore.virtualstore.orderservice.query.dataaccess.repository;

import org.example.explore.virtualstore.orderservice.query.dataaccess.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends CrudRepository<OrderEntity, String> {
    Optional<OrderEntity> findByOrderId(String orderId);
}
