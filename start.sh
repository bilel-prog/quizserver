#!/bin/bash

# Render.com Environment Variable Setup
# This script will be executed by Render to set up PostgreSQL environment variables

# Set Spring Profile to production
export SPRING_PROFILES_ACTIVE=production

# If DATABASE_URL exists, parse it into individual components for Spring Boot
if [ -n "$DATABASE_URL" ]; then
    echo "DATABASE_URL found: $DATABASE_URL"
    
    # Render provides DATABASE_URL in format: postgres://user:password@host:port/database
    # Extract components for Spring Boot
    export DB_URL="$DATABASE_URL"
    
    # Use the full DATABASE_URL directly since Spring Boot can parse it
    export SPRING_DATASOURCE_URL="$DATABASE_URL"
    export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
    export SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
    
    echo "Configured PostgreSQL with DATABASE_URL"
else
    echo "No DATABASE_URL found - using fallback configuration"
fi

# Database configuration will be automatically provided by Render via DATABASE_URL
# Additional environment variables for explicit configuration
export DB_DRIVER=org.postgresql.Driver
export DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Start the application
java -Dspring.profiles.active=production -Dserver.port=${PORT:-10000} -jar app.jar
