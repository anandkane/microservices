package org.example.explore.virtualstore.productservice.core.errorhandlers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private Date timestamp;

    private String message;

    public ErrorMessage(String message) {
        this.timestamp = new Date();
        this.message = message;
    }
}
