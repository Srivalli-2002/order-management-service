package org.example.orderservice.controller;

import org.example.orderservice.entity.CustomerType;
import org.example.orderservice.entity.Order;
import org.example.orderservice.service.OrderManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderManagementControllerTests {

    @Mock
    private OrderManagementService service;

    @InjectMocks
    private OrderManagementController controller;

    @Test
    void createOrder_success() {

        Order order = new Order();
        order.setCustomerType(CustomerType.REGULAR);
        order.setAmount(BigDecimal.valueOf(100));
        order.setOrderDate(LocalDateTime.now());

        when(service.createOrder(order)).thenReturn(order);

        Order result = controller.createOrder(order);

        assertNotNull(result);
        verify(service).createOrder(order);
    }

    @Test
    void getOrder_success() {

        Order order = new Order();
        when(service.getOrder(1L)).thenReturn(order);

        Order result = controller.getOrder(1L);

        assertEquals(order, result);
        verify(service).getOrder(1L);
    }

    @Test
    void getOrdersByMonth_success() {

        Order order = new Order();
        when(service.getOrdersByMonth(YearMonth.parse("2024-05")))
                .thenReturn(List.of(order));

        List<Order> result = controller.getOrdersByMonth("2024-05");

        assertEquals(1, result.size());
        verify(service).getOrdersByMonth(YearMonth.parse("2024-05"));
    }
}