package org.example.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.entity.CustomerType;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.OrderManagementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderManagementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderManagementRepository repository;

    @Test
    void createOrder_success() throws Exception {

        Order order = new Order();
        order.setCustomerType(CustomerType.REGULAR);
        order.setAmount(BigDecimal.valueOf(100));
        order.setOrderDate(LocalDateTime.of(2024, 5, 10, 10, 0));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerType").value("REGULAR"))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void getOrder_success() throws Exception {

        Order order = new Order();
        order.setCustomerType(CustomerType.REGULAR);
        order.setAmount(BigDecimal.valueOf(200));
        order.setOrderDate(LocalDateTime.now());

        Order saved = repository.save(order);

        mockMvc.perform(get("/orders/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getOrder_notFound() throws Exception {

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrdersByMonth_success() throws Exception {

        Order order = new Order();
        order.setCustomerType(CustomerType.REGULAR);
        order.setAmount(BigDecimal.valueOf(150));
        order.setOrderDate(LocalDateTime.of(2024, 5, 15, 10, 0));

        repository.save(order);

        mockMvc.perform(get("/orders")
                        .param("month", "2024-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createOrder_validationFailure() throws Exception {

        Order order = new Order(); // missing fields

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.customerType").exists());
    }

    @Test
    void createOrder_invalidEnum() throws Exception {

        String json = """
            {
              "customerType": "INVALID",
              "amount": 100,
              "orderDate": "2024-05-10T10:00:00"
            }
        """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.customerType").exists());
    }

    @Test
    void createOrder_invalidDate() throws Exception {

        String json = """
            {
              "customerType": "REGULAR",
              "amount": 100,
              "orderDate": "invalid-date"
            }
        """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}