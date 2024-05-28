package org.example.explore.virtualstore.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails {
    private String cardNumber;
    private int validUntilMonth;
    private int validUntilYear;
    private String cvv;
}
