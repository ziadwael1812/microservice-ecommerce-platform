package com.example.microservice.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // General discovery client, works with Eureka, Consul, etc.
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // Define beans for circuit breakers, custom filters, etc. here if needed
    /* Example Resilience4j configuration bean
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10L))
                        .permittedNumberOfCallsInHalfOpenState(5)
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .build());
    }
    */
}
