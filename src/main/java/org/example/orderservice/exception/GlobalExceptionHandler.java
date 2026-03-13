package org.example.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleRuntime(RuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidEnum(HttpMessageNotReadableException ex) {

        Map<String, String> errors = new HashMap<>();

        String message = ex.getMostSpecificCause().getMessage();

        if (message.contains("CustomerType")) {
            errors.put("customerType", "Invalid value. Allowed values: REGULAR, PREMIUM");
        }
        else if (message.contains("orderDate")) {
            errors.put("orderDate", "Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm:ss");
        }
        else {
            errors.put("error", message);
        }

        return errors;
    }
}