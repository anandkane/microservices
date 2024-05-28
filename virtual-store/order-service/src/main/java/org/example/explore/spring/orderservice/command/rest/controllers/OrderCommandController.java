package org.example.explore.virtualstore.orderservice.command.rest.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.example.explore.virtualstore.orderservice.command.commands.CreateOrderCommand;
import org.example.explore.virtualstore.orderservice.core.rest.models.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.example.explore.virtualstore.orderservice.core.constants.GenericConstants.USER_ID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Slf4j
public class OrderCommandController {
    private final Environment environment;
    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody Order order) {
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(USER_ID)
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .addressId(order.getAddressId())
                .build();

        log.info("Sending create order command: {}", createOrderCommand);
        Object commandResponse = commandGateway.sendAndWait(createOrderCommand);
        log.info("Create order command sent: {}", createOrderCommand);
        return ResponseEntity.ok(commandResponse);
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok(
                "Order service is up and running on port: " + environment.getProperty("local.server.port"));
    }

}
