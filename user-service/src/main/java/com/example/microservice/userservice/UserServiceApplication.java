package com.example.microservice.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; // If using Feign
// import org.springframework.context.annotation.Bean; // For custom beans
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Example bean
// import org.springframework.security.crypto.password.PasswordEncoder; // Example bean

@SpringBootApplication
@EnableDiscoveryClient // To register with Eureka or other discovery services
@EnableFeignClients // If this service calls other services using Feign
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    /* Example of a bean that might be needed
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */
}
