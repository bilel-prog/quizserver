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

# First try to copy from root target directory
COPY --from=build /build/target/quizserver-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
