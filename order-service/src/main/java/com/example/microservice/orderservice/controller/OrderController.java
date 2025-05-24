package com.example.microservice.orderservice.controller;

import com.example.microservice.orderservice.dto.OrderRequest;
import com.example.microservice.orderservice.dto.OrderResponse;
import com.example.microservice.orderservice.entity.OrderStatus;
import com.example.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest,
                                                 @RequestHeader("Authorization") String authToken) {
        try {
            OrderResponse orderResponse = orderService.createOrder(orderRequest, authToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (RuntimeException e) {
            // More specific exception handling can be added here
            return ResponseEntity.badRequest().body(null); // Or a DTO with error message
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId,
                                                              @RequestHeader("Authorization") String authToken) {
        try {
            List<OrderResponse> orders = orderService.getOrdersByUserId(userId, authToken);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber,
                                                               @RequestHeader("Authorization") String authToken) {
        try {
            OrderResponse orderResponse = orderService.getOrderByOrderNumber(orderNumber, authToken);
            return ResponseEntity.ok(orderResponse);
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }
    }

    @PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderNumber,
                                                         @RequestParam OrderStatus status,
                                                         @RequestHeader("Authorization") String authToken) {
        // Assuming only admin/internal service can call this. Token validation for role needed.
        try {
            OrderResponse orderResponse = orderService.updateOrderStatus(orderNumber, status, authToken);
            return ResponseEntity.ok(orderResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
