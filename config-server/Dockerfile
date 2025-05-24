# Dockerfile for config-server
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy Maven wrapper and pom.xml to leverage Docker cache for dependencies
# Assuming mvnw and .mvn are in the config-server directory itself or copied appropriately before build
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Set the entrypoint
# The JAR name might vary based on the build, ensure it matches
ENTRYPOINT ["java","-jar","target/config-server-0.0.1-SNAPSHOT.jar"]

EXPOSE 8888
