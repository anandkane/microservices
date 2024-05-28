package org.example.explore.virtualstore.paymentservice.query.dataccess.repository;

import org.example.explore.virtualstore.paymentservice.query.dataccess.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, String>{
    Optional<PaymentEntity> findByOrderId(String orderId);
    Optional<PaymentEntity> findByPaymentId(String paymentId);
}
