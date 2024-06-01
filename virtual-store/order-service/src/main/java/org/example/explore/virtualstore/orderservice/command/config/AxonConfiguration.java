package org.example.explore.virtualstore.orderservice.command.config;

import org.axonframework.config.EventProcessingConfigurer;
import org.example.explore.virtualstore.orderservice.command.errorhandlers.OrderServiceEventsErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    @Autowired
    void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("order-group", config ->
                new OrderServiceEventsErrorHandler());
    }
}
