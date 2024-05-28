package org.example.explore.virtualstore.orderservice.command.dataaccess.repositories;

import org.example.explore.virtualstore.orderservice.command.dataaccess.entities.OrderLookupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderLookupRepository extends CrudRepository<OrderLookupEntity, String> {
    Optional<OrderLookupEntity> findByOrderId(String orderId);
}
