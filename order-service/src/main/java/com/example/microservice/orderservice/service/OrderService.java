package com.example.microservice.orderservice.service;

import com.example.microservice.orderservice.client.ProductClient;
import com.example.microservice.orderservice.client.UserClient;
import com.example.microservice.orderservice.dto.*;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.entity.OrderItem;
import com.example.microservice.orderservice.entity.OrderStatus;
import com.example.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient; // For fetching user ID from token
    private final ProductClient productClient; // For fetching product details

    public OrderResponse createOrder(OrderRequest orderRequest, String authToken) {
        // 1. Get User ID from Token via UserClient
        Long userId = userClient.getUserIdFromToken(authToken);
        if (userId == null) {
            throw new RuntimeException("Invalid token or user not found.");
        }

        // 2. Validate user existence (optional, depends on UserClient capability)
        // boolean userExists = userClient.userExists(userId, authToken);
        // if (!userExists) {
        //     throw new RuntimeException("User with ID " + userId + " not found.");
        // }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(orderRequest.getShippingAddress());

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemRequest -> {
                    // 3. Fetch Product Details (especially name and confirm price) via ProductClient
                    ProductResponse productDetails = productClient.getProductById(itemRequest.getProductId(), authToken);
                    if (productDetails == null) {
                        throw new RuntimeException("Product with ID " + itemRequest.getProductId() + " not found.");
                    }
                    // Basic check for stock, more complex logic might be in product service
                    if (productDetails.getStockQuantity() < itemRequest.getQuantity()){
                        throw new RuntimeException("Not enough stock for product ID: " + itemRequest.getProductId());
                    }

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemRequest.getProductId());
                    orderItem.setProductName(productDetails.getName()); // Use name from product service
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPriceAtPurchase(itemRequest.getPrice()); // Or use productDetails.getPrice() for current price
                    orderItem.setOrder(order);
                    return orderItem;
                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        log.info("Order {} is saved for user {}", savedOrder.getOrderNumber(), userId);

        // TODO: Potentially call Product Service to update stock (event-driven or direct)

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId, String authToken) {
        // Validate token or ensure the requesting user matches userId if necessary
        Long requestingUserId = userClient.getUserIdFromToken(authToken);
        if (!requestingUserId.equals(userId)){
            // Or throw an access denied exception based on security policy
            throw new RuntimeException("User not authorized to view these orders."); 
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    public OrderResponse getOrderByOrderNumber(String orderNumber, String authToken) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new RuntimeException("Order not found with order number: " + orderNumber);
        }
        // Validate that the user requesting is the one who owns the order
        Long requestingUserId = userClient.getUserIdFromToken(authToken);
        if(!order.getUserId().equals(requestingUserId)){
             throw new RuntimeException("User not authorized to view this order.");
        }
        return mapToOrderResponse(order);
    }

     public OrderResponse updateOrderStatus(String orderNumber, OrderStatus newStatus, String authToken) {
        // Here, authToken might be used to ensure only an admin or specific roles can update status
        // For simplicity, we'll assume an admin role is checked elsewhere or this is an internal call
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new RuntimeException("Order not found with order number: " + orderNumber);
        }
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} status updated to {}", orderNumber, newStatus);
        return mapToOrderResponse(updatedOrder);
    }


    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(oi -> OrderItemResponse.builder()
                        .id(oi.getId())
                        .productId(oi.getProductId())
                        .productName(oi.getProductName())
                        .quantity(oi.getQuantity())
                        .priceAtPurchase(oi.getPriceAtPurchase())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .orderItems(itemResponses)
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .build();
    }
}
