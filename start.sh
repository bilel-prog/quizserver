#!/bin/bash

# Render.com Environment Variable Setup
# This script will be executed by Render to set up PostgreSQL environment variables

# Set Spring Profile to production
export SPRING_PROFILES_ACTIVE=production

# Check if DATABASE_URL exists and debug
echo "DATABASE_URL environment variable: ${DATABASE_URL:-'NOT SET'}"
printenv | grep -i database || echo "No DATABASE environment variables found"

# If DATABASE_URL exists, use it directly
if [ -n "$DATABASE_URL" ]; then
    echo "DATABASE_URL found - Spring Boot will auto-configure PostgreSQL"
    # Let Spring Boot handle DATABASE_URL directly
    export SPRING_DATASOURCE_URL="$DATABASE_URL"
else
    echo "No DATABASE_URL found - using production profile PostgreSQL defaults"
    # Render should provide DATABASE_URL, but if not, we have defaults in production profile
fi

# Ensure PostgreSQL driver and dialect are set for production
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

echo "Starting application with production profile..."
echo "Active profile: $SPRING_PROFILES_ACTIVE"
echo "Port: ${PORT:-10000}"

# Start the application
java -Dspring.profiles.active=production -Dserver.port=${PORT:-10000} -jar app.jar
