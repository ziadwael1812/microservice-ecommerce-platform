# Dockerfile for api-gateway
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Set the entrypoint
ENTRYPOINT ["java","-jar","target/api-gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
