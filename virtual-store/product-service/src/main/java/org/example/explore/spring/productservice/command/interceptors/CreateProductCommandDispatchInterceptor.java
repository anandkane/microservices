package org.example.explore.virtualstore.productservice.command.interceptors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.example.explore.virtualstore.productservice.command.commands.CreateProductCommand;
import org.example.explore.virtualstore.productservice.query.dataaccess.repositories.ProductsLookupRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@AllArgsConstructor
@Slf4j
public class CreateProductCommandDispatchInterceptor
        implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final ProductsLookupRepository productsLookupRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if (command.getPayload().getClass().isAssignableFrom(CreateProductCommand.class)) {
                CreateProductCommand payload = (CreateProductCommand) command.getPayload();
                log.info("Intercepted command: {}", payload);
                if (productsLookupRepository.findByTitleOrProductId(payload.getTitle(), payload.getProductId()).isPresent()) {
                    throw new IllegalArgumentException("Product with the same ID or title already exists");
                }
            }
            return command;
        };
    }
}
