package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.CustomerType;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.OrderManagementRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderManagementService {

    private final OrderManagementRepository repository;

    public Order createOrder(Order order) {
        return repository.save(order);
    }

    public Order getOrder(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Map<YearMonth, BigDecimal> calculateMonthlyRevenue() {
        return repository.findAll().stream()
                .filter(order -> order.getAmount() != null && order.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(
                        order -> YearMonth.from(order.getOrderDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                this::applyDiscount,
                                BigDecimal::add
                        )
                ));
    }

    private BigDecimal applyDiscount(Order order) {
        BigDecimal amount = order.getAmount();
        if (order.getCustomerType() == CustomerType.PREMIUM) {
            return amount.multiply(BigDecimal.valueOf(0.9));
        }
        return amount;
    }

    public List<Order> getOrdersByMonth(YearMonth month) {
        return repository.findAll().stream()
                .filter(order -> order.getOrderDate() != null)
                .filter(order -> YearMonth.from(order.getOrderDate()).equals(month))
                .toList();
    }
}