package org.example.explore.virtualstore.productservice.query.errorhandlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryExecutionException;
import org.example.explore.virtualstore.productservice.query.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice(basePackages = "org.example.explore.virtualstore.productservice.query")
@Slf4j
public class ProductQueryErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(ProductNotFoundException ex) {
        log.error("Product not found: ", ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(QueryExecutionException ex) {
        log.error("Exception occurred: ", ex);
        if (ex.getCause().getClass().isAssignableFrom(ProductNotFoundException.class)) {
            return new ResponseEntity<>(new org.example.explore.virtualstore.productservice.core.errorhandlers.ErrorMessage(
                    ex.getCause().getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new org.example.explore.virtualstore.productservice.core.errorhandlers.ErrorMessage(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Exception occurred: ", ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
