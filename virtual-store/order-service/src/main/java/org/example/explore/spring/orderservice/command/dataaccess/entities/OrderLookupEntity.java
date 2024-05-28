package org.example.explore.virtualstore.orderservice.command.dataaccess.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_lookup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLookupEntity {
    @Id
    private String orderId;
}
