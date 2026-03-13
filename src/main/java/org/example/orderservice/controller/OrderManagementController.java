package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.entity.Order;
import org.example.orderservice.service.OrderManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderManagementService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderManagementService.getOrder(id);
    }

    @GetMapping
    public List<Order> getOrdersByMonth(@RequestParam String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        return orderManagementService.getOrdersByMonth(yearMonth);
    }
}