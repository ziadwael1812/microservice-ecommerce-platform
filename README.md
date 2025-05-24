# Microservice E-Commerce Platform

This project demonstrates a microservice architecture for an e-commerce application using Spring Boot, Spring Cloud, Docker, and other modern technologies.

## Architecture Overview

- **User Service**: Manages user data, authentication, and authorization.
- **Product Service**: Handles the product catalog, inventory, and pricing.
- **Order Service**: Manages customer orders, payment processing (simulated), and communicates with other services.
- **API Gateway (Spring Cloud Gateway)**: Single entry point for all client requests, handles routing and some cross-cutting concerns like security.
- **Service Discovery (Netflix Eureka)**: Allows services to find and communicate with each other dynamically.
- **Configuration Server (Spring Cloud Config)**: Centralized configuration management for all microservices.
- **Messaging Queue (RabbitMQ/Kafka)**: For asynchronous communication between services (e.g., order processing).
- **Circuit Breaker (Resilience4j)**: Improves fault tolerance by preventing cascading failures.
- **Observability**: 
    - Centralized Logging (ELK Stack - conceptual, not fully implemented in this base setup)
    - Metrics (Micrometer + Prometheus + Grafana - conceptual, not fully implemented in this base setup)

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Cloud
    - Spring Cloud Gateway
    - Spring Cloud Netflix Eureka
    - Spring Cloud Config Server
    - Spring Cloud OpenFeign (for inter-service communication)
- Resilience4j (Circuit Breaker)
- RabbitMQ (or Kafka, for messaging)
- Docker & Docker Compose
- PostgreSQL (for individual service databases)
- Maven (for build and dependency management)

## Project Structure

```
microservice-ecommerce-platform/
├── api-gateway/                # Spring Cloud Gateway
├── config-server/              # Spring Cloud Config Server
├── service-registry/           # Eureka Service Registry
├── user-service/               # User microservice
├── product-service/            # Product microservice
├── order-service/              # Order microservice
├── common/                     # Shared DTOs, utilities, etc.
├── docker-compose.yml          # Docker Compose file to run all services
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- Java 17+ (JDK)
- Docker & Docker Compose
- Maven
- Git

### Build & Run

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/ziadwael1812/microservice-ecommerce-platform.git
    cd microservice-ecommerce-platform
    ```

2.  **Build all Maven projects:**
    A script can be created for this, or build each service individually:
    ```bash
    # (In the root directory)
    mvn clean install -f user-service/pom.xml
    mvn clean install -f product-service/pom.xml
    mvn clean install -f order-service/pom.xml
    mvn clean install -f api-gateway/pom.xml
    mvn clean install -f service-registry/pom.xml
    mvn clean install -f config-server/pom.xml
    # ... and any other modules like 'common'
    ```
    Alternatively, use a parent POM if implemented.

3.  **Run using Docker Compose:**
    This is the recommended way to run all services together.
    ```bash
    docker-compose up --build
    ```

4.  **Accessing Services:**
    -   **API Gateway**: `http://localhost:8080` (or the port configured in `api-gateway`)
    -   **Eureka Dashboard**: `http://localhost:8761`
    -   **Config Server**: `http://localhost:8888`
    -   Individual services will be routed through the API Gateway.

## Configuration

-   Centralized configuration is managed by the `config-server`. Config files for each service (e.g., `user-service.yml`, `product-service.yml`) should be placed in a Git repository that the `config-server` points to (typically defined in `config-server/src/main/resources/application.yml`). For this example, local file system based config might be used for simplicity initially.

## Further Development

-   Implement full ELK stack for logging.
-   Integrate Prometheus and Grafana for monitoring.
-   Add distributed tracing (e.g., with Spring Cloud Sleuth & Zipkin).
-   Enhance security with OAuth2/OIDC.
-   Write comprehensive unit and integration tests.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.
