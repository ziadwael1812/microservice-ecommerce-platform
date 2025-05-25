# Microservice E-Commerce Platform

This project demonstrates a microservice architecture for an e-commerce application using Spring Boot, Spring Cloud, Docker, and other modern technologies.

## 🏗️ Architecture Overview


<!-- Example placeholder for an architecture diagram -->
<!-- ![Architecture Diagram](diagrams/microservice_architecture.png) -->

Key components include:

- **User Service**: Manages user data, authentication, and authorization.
- **Product Service**: Handles the product catalog, inventory, and pricing.
- **Order Service**: Manages customer orders, payment processing (simulated), and communicates with other services.
- **API Gateway (Spring Cloud Gateway)**: Single entry point for all client requests, handles routing and some cross-cutting concerns like security.
- **Service Discovery (Netflix Eureka)**: Allows services to find and communicate with each other dynamically.
- **Configuration Server (Spring Cloud Config)**: Centralized configuration management for all microservices.
- **Messaging Queue (RabbitMQ/Kafka)**: For asynchronous communication between services (e.g., order processing).
- **Circuit Breaker (Resilience4j)**: Improves fault tolerance by preventing cascading failures.
- **Observability (Conceptual for this setup):**
    - Centralized Logging (e.g., ELK Stack)
    - Metrics (e.g., Micrometer + Prometheus + Grafana)

## 🛠️ Tech Stack

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

## 📂 Project Structure

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

## 🚀 Getting Started

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
    It's recommended to build modules in an order that respects dependencies (e.g., `common` module first if others depend on it).
    ```bash
    # From the root directory of the project:
    # (Build common module if it exists and is a dependency for others)
    # mvn clean install -f common/pom.xml 
    mvn clean install -f user-service/pom.xml
    mvn clean install -f product-service/pom.xml
    mvn clean install -f order-service/pom.xml
    # ... build other service modules ...
    mvn clean install -f service-registry/pom.xml
    mvn clean install -f config-server/pom.xml
    mvn clean install -f api-gateway/pom.xml
    ```
    *(Note: A parent POM in the root to build all modules with a single command would be a good future enhancement.)*

3.  **Run using Docker Compose (Recommended):**
    This will build images (if not present or if `--build` is used) and start all services.
    ```bash
    docker-compose up --build
    ```

4.  **Accessing Services:**
    -   **API Gateway**: `http://localhost:8080` (or the port configured in `api-gateway`) - All service interactions should go through here.
    -   **Eureka Dashboard**: `http://localhost:8761`
    -   **Config Server**: `http://localhost:8888`

## ⚙️ Configuration

-   Centralized configuration is managed by the `config-server`. Config files for each service (e.g., `user-service.yml`, `product-service.yml`) should be placed in a Git repository that the `config-server` points to. This is typically defined in `config-server/src/main/resources/application.yml`. For simplicity in this project, configuration files might be sourced locally by the config server if a remote Git repo is not set up.

## 🌱 Further Development & Enhancements

-   Implement a full ELK stack (Elasticsearch, Logstash, Kibana) for centralized logging.
-   Integrate Prometheus and Grafana for comprehensive metrics and monitoring dashboards.
-   Add distributed tracing using Spring Cloud Sleuth & Zipkin (or similar) to trace requests across services.
-   Enhance security measures, potentially with OAuth2/OIDC for inter-service and user authentication.
-   Write comprehensive unit, integration, and contract tests for all services.
