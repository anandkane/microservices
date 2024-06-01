package org.example.explore.virtualstore.userservice.query.handler;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.example.explore.virtualstore.core.dto.PaymentDetails;
import org.example.explore.virtualstore.core.query.user.queries.FetchUserPaymentDetailsQuery;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceEventsHandler {

    @QueryHandler
    public PaymentDetails handle(FetchUserPaymentDetailsQuery query) {
        log.info("Handling FetchUserPaymentDetailsQuery: {}", query);
        return PaymentDetails.builder()
                .cardNumber("1234-5678-1234-5678")
                .cvv("123")
                .validUntilMonth(12)
                .validUntilYear(2023)
                .build();
    }
}
