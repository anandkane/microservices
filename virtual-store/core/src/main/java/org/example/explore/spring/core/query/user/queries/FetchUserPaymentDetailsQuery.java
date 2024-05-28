package org.example.explore.virtualstore.core.query.user.queries;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchUserPaymentDetailsQuery {
    private String userId;
}
