package org.example.explore.virtualstore.orderservice.command.errorhandlers;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;


public class OrderServiceEventsErrorHandler implements ListenerInvocationErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceEventsErrorHandler.class);

    @Override
    public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event,
            @Nonnull EventMessageHandler eventHandler) throws Exception {
        log.error("Error handling event: {}", event.getPayloadType(), exception);
        throw exception;
    }
}
