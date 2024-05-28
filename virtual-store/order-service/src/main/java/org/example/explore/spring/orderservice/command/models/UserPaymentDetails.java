package org.example.explore.virtualstore.orderservice.command.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentDetails {
    private String name;
    private String cardNumber;
    private int validUntilMonth;
    private int validUntilYear;
    private String cvv;
}
