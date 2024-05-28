package org.example.explore.virtualstore.orderservice.command.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.example.explore.virtualstore.core.command.payment.commands.ProcessPaymentCommand;
import org.example.explore.virtualstore.core.command.payment.commands.ReversePaymentCommand;
import org.example.explore.virtualstore.core.command.product.commands.CancelProductReservationCommand;
import org.example.explore.virtualstore.core.command.product.commands.ReserveProductCommand;
import org.example.explore.virtualstore.core.dto.PaymentDetails;
import org.example.explore.virtualstore.core.event.payment.events.PaymentProcessedEvent;
import org.example.explore.virtualstore.core.event.payment.events.PaymentReversedEvent;
import org.example.explore.virtualstore.core.event.product.events.ProductReservationCancelledEvent;
import org.example.explore.virtualstore.core.event.product.events.ProductReservedEvent;
import org.example.explore.virtualstore.core.query.user.queries.FetchUserPaymentDetailsQuery;
import org.example.explore.virtualstore.orderservice.command.commands.ApproveOrderCommand;
import org.example.explore.virtualstore.orderservice.command.commands.RejectOrderCommand;
import org.example.explore.virtualstore.orderservice.core.events.OrderApprovedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderCreatedEvent;
import org.example.explore.virtualstore.orderservice.core.events.OrderRejectedEvent;
import org.example.explore.virtualstore.orderservice.core.queries.FindOrderByOrderIdQuery;
import org.example.explore.virtualstore.orderservice.core.rest.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

//@Slf4j
@Saga
public class OrderSaga {
    Logger log = LoggerFactory.getLogger(OrderSaga.class);
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        log.info("Handling OrderCreatedEvent: {}", event);
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder().orderId(event.getOrderId())
                .productId(event.getProductId()).quantity(event.getQuantity()).userId(event.getUserId()).build();

        log.info("Sending ReserveProductCommand: {}", reserveProductCommand);
        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("Failed to reserve product: {}", commandResultMessage.exceptionResult());
                sendRejectOrderCommand(event.getOrderId(), commandResultMessage.exceptionResult().getMessage());
            } else {
                log.info("Product reserved successfully: {}", commandResultMessage.getPayload());
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(ProductReservedEvent event) {
        log.info("Handling ProductReservedEvent: {}", event);

        StringBuilder reason = new StringBuilder();
        PaymentDetails paymentDetails = getPaymentDetails(event.getUserId(), reason);
//        // To generate failure in payment processing
//        paymentDetails.setCardNumber(null);
        if (paymentDetails == null) {
            sendCancelProductReservationCommand(event.getOrderId(), event.getProductId(), event.getQuantity(),
                    reason.toString());
            return;
        }

        String paymentId = sendProcessPaymentCommand(event.getOrderId(), paymentDetails, reason);
        if (paymentId == null) {
            sendCancelProductReservationCommand(event.getOrderId(), event.getProductId(), event.getQuantity(),
                    reason.toString());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(ProductReservationCancelledEvent event) {
        log.info("Handling ProductReservationCancelledEvent: {}", event);
        sendRejectOrderCommand(event.getOrderId(), event.getMessage());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(PaymentProcessedEvent event) {
        log.info("Handling PaymentProcessedEvent: {}", event);
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(event.getOrderId());
        commandGateway.send(approveOrderCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("Failed to approve order: {}", commandResultMessage.exceptionResult());
                sendReversePaymentCommand(event.getPaymentId(), event.getOrderId(),
                        commandResultMessage.exceptionResult().getMessage());
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(PaymentReversedEvent event) {
        log.info("Handling PaymentReversedEvent: {}", event);
        FindOrderByOrderIdQuery findOrderByOrderIdQuery = new FindOrderByOrderIdQuery(event.getOrderId());
        Order order = queryGateway.query(findOrderByOrderIdQuery, ResponseTypes.instanceOf(Order.class)).join();
        sendCancelProductReservationCommand(event.getOrderId(), order.getProductId(), order.getQuantity(),
                event.getMessage());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderApprovedEvent event) {
        log.info("Order approved: {}", event.getOrderId());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderRejectedEvent event) {
        log.info("Order rejected: {}", event.getOrderId());
    }

    private PaymentDetails getPaymentDetails(String userId, final StringBuilder reason) {
        log.info("Querying user service for payment details of user: {}", userId);
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = FetchUserPaymentDetailsQuery.builder()
                .userId(userId).build();

        PaymentDetails paymentDetails = null;
        try {
            paymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery,
                    ResponseTypes.instanceOf(PaymentDetails.class)).join();
            log.info("Payment details received: {}", paymentDetails);
        } catch (Exception e) {
            reason.append(e.getMessage());
            log.error("Failed to fetch user payment details: {}", e.getMessage());
        }

        if (paymentDetails == null) {
            // start compensating transaction
            reason.append("Failed to fetch user payment details");
        }
        return paymentDetails;
    }

    private String sendProcessPaymentCommand(String orderId, PaymentDetails paymentDetails,
            final StringBuilder reason) {
        String paymentId = null;
        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder().orderId(orderId)
                .paymentId(UUID.randomUUID().toString()).paymentDetails(paymentDetails).build();
        try {
            log.info("Sending ProcessPaymentCommand: {}", processPaymentCommand);
            paymentId = commandGateway.sendAndWait(processPaymentCommand, 10000, TimeUnit.MILLISECONDS);
            log.info("Payment processed successfully with paymentId: {}", paymentId);
        } catch (Exception e) {
            log.error("Payment processing failed with exception: {}", e.getMessage());
            reason.append(e.getMessage());
        }
        return paymentId;
    }

    private void sendRejectOrderCommand(String orderId, String reason) {
        log.info("Sending RejectOrderCommand: {}", orderId);
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderId, reason);
        commandGateway.send(rejectOrderCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("Failed to reject order: {}", commandResultMessage.exceptionResult());
                // start compensating transaction
            } else {
                log.info("RejectOrderCommand sent successfully: {}", commandResultMessage.getPayload());
            }
        });
    }

    private void sendCancelProductReservationCommand(String orderId, String productId, int quantity, String reason) {
        log.info("Sending CancelProductReservationCommand: productId={}, quantity={}", productId, quantity);
        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(orderId).productId(productId).quantity(quantity).message(reason).build();
        commandGateway.send(cancelProductReservationCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                log.error("Failed to cancel product reservation: {}", commandResultMessage.exceptionResult());
                // start compensating transaction
            } else {
                log.info("CancelProductReservationCommand sent successfully: {}", commandResultMessage.getPayload());
            }
        });
    }

    private void sendReversePaymentCommand(String paymentId, String orderId, String reason) {
        log.info("Sending ReversePaymentCommand: {}", paymentId);
        ReversePaymentCommand reversePaymentCommand = ReversePaymentCommand.builder()
                .paymentId(paymentId).orderId(orderId).message(reason).build();
        commandGateway.send(reversePaymentCommand, (commandMessage1, commandResultMessage1) -> {
            if (commandResultMessage1.isExceptional()) {
                log.error("Failed to reverse payment: {}", commandResultMessage1.exceptionResult());
                // start compensating transaction
            } else {
                log.info("Payment reversed successfully: {}", commandResultMessage1.getPayload());
            }
        });
    }
}
