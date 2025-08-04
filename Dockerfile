# Multi-stage build for Spring Boot application
FROM maven:3.8.4-openjdk-17-slim AS build

# Set working directory
WORKDIR /build

# Copy everything
COPY . .

# Debug: List the structure
RUN echo "=== Repository structure ===" && \
    ls -la && \
    find . -name "pom.xml" -type f

# Build the application
RUN if [ -f "pom.xml" ]; then \
        echo "Building from root directory" && \
        mvn clean package -DskipTests; \
    elif [ -f "quizserver/pom.xml" ]; then \
        echo "Building from quizserver subdirectory" && \
        cd quizserver && \
        mvn clean package -DskipTests; \
    else \
        echo "ERROR: No pom.xml found!" && \
        exit 1; \
    fi

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR file from the quizserver subdirectory target folder
COPY --from=build /build/quizserver/target/quizserver-0.0.1-SNAPSHOT.jar /app/app.jar

# Copy startup script
COPY --from=build /build/start.sh /app/start.sh
RUN chmod +x /app/start.sh

# Expose port
EXPOSE 8080

# Run the application using startup script
CMD ["/app/start.sh"]
