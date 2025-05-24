package com.example.microservice.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

// Assuming user-service has an endpoint to get basic user details by ID
// and another to validate the token and get user ID.
@FeignClient(name = "user-service", url = "${services.user.url}") // Name should match user-service application name in Eureka
public interface UserClient {

    // This endpoint is hypothetical, needs to exist in User Service
    @GetMapping("/api/users/id")
    Long getUserIdFromToken(@RequestHeader("Authorization") String token);

    // This is also hypothetical, assuming User Service can provide a simple user check by ID
    @GetMapping("/api/users/internal/{userId}/exists")
    boolean userExists(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String token);

}
