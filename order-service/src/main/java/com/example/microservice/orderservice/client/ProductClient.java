package com.example.microservice.orderservice.client;

import com.example.microservice.orderservice.dto.ProductResponse; // Assuming a similar DTO exists or is created in common module
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-service", url = "${services.product.url}") // Name should match product-service application name in Eureka
public interface ProductClient {

    // This endpoint should exist in Product Service
    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long productId, @RequestHeader("Authorization") String token);

}
