package org.example.orderservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntime_success() {
        RuntimeException ex = new RuntimeException("Order not found");

        String result = handler.handleRuntime(ex);

        assertEquals("Order not found", result);
    }

    @Test
    void handleValidation_success() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(new FieldError("obj", "amount", "Amount is required"))
        );

        Map<String, String> result = handler.handleValidation(ex);

        assertEquals("Amount is required", result.get("amount"));
    }

    @Test
    void handleInvalidEnum_customerType() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("",
                        new RuntimeException("CustomerType error"));

        Map<String, String> result = handler.handleInvalidEnum(ex);

        assertTrue(result.containsKey("customerType"));
    }

    @Test
    void handleInvalidEnum_orderDate() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("",
                        new RuntimeException("orderDate error"));

        Map<String, String> result = handler.handleInvalidEnum(ex);

        assertTrue(result.containsKey("orderDate"));
    }

    @Test
    void handleInvalidEnum_generic() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("",
                        new RuntimeException("some other error"));

        Map<String, String> result = handler.handleInvalidEnum(ex);

        assertTrue(result.containsKey("error"));
    }
}