# Multi-stage build for Spring Boot application
FROM maven:3.8.4-openjdk-17-slim AS build

# Set working directory
WORKDIR /build

# Copy everything
COPY . .

# Navigate to the Spring Boot project directory and build
WORKDIR /build/quizserver/quizserver
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar from build stage
COPY --from=build /build/quizserver/quizserver/target/quizserver-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
