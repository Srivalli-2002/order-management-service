package org.example.orderservice.repository;

import org.example.orderservice.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderManagementRepository {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private long idCounter = 1;

    public Order save(Order order) {
        order.setId(idCounter++);
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
}