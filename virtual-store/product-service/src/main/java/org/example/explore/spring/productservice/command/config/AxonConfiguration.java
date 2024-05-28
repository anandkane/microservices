package org.example.explore.virtualstore.productservice.command.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.example.explore.virtualstore.productservice.command.interceptors.CreateProductCommandDispatchInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    @Autowired
    public void registerCreateProductCommandDispatchInterceptor(CommandBus commandBus,
            CreateProductCommandDispatchInterceptor interceptor) {
        commandBus.registerDispatchInterceptor(interceptor);
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
//        configurer.registerListenerInvocationErrorHandler("product-group",
//                                                          config -> new ProductServiceEventsErrorHandler());
        configurer.registerListenerInvocationErrorHandler("product-group",
                                                          config -> PropagatingErrorHandler.instance());
    }

}
