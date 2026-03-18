package org.example.orderservice.service;

import org.example.orderservice.entity.CustomerType;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.OrderManagementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderManagementServiceTests {

    @Mock
    private OrderManagementRepository repository;

    @InjectMocks
    private OrderManagementService service;

    @Test
    void createOrder_success() {
        Order order = new Order();
        when(repository.save(order)).thenReturn(order);

        Order result = service.createOrder(order);

        assertNotNull(result);
        verify(repository).save(order);
    }

    @Test
    void getOrder_success() {
        Order order = new Order();
        when(repository.findById(1L)).thenReturn(Optional.of(order));

        Order result = service.getOrder(1L);

        assertEquals(order, result);
    }

    @Test
    void getOrder_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getOrder(1L));

        assertEquals("Order not found", ex.getMessage());
    }

    @Test
    void calculateMonthlyRevenue_withDiscount() {
        Order order = new Order();
        order.setCustomerType(CustomerType.PREMIUM);
        order.setAmount(BigDecimal.valueOf(100));
        order.setOrderDate(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(order));

        var result = service.calculateMonthlyRevenue();

        assertFalse(result.isEmpty());
        BigDecimal value = result.values().iterator().next();
        assertEquals(BigDecimal.valueOf(90.0), value);
    }

    @Test
    void getOrdersByMonth_success() {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.of(2024, 5, 10, 10, 0));

        when(repository.findAll()).thenReturn(List.of(order));

        List<Order> result = service.getOrdersByMonth(YearMonth.of(2024, 5));

        assertEquals(1, result.size());
    }
}