#!/bin/bash

# Render.com Environment Variable Setup
# This script will be executed by Render to set up PostgreSQL environment variables

# Set Spring Profile to production
export SPRING_PROFILES_ACTIVE=production

# Check if DATABASE_URL exists and debug
echo "DATABASE_URL environment variable: ${DATABASE_URL:-'NOT SET'}"
printenv | grep -i database || echo "No DATABASE environment variables found"

# If DATABASE_URL exists, parse it into components
if [ -n "$DATABASE_URL" ]; then
    echo "DATABASE_URL found - parsing into components"
    
    # DATABASE_URL format: postgresql://username:password@host:port/database
    # Extract components using parameter expansion and sed
    
    # Remove protocol prefix
    URL_NO_PROTOCOL="${DATABASE_URL#postgresql://}"
    URL_NO_PROTOCOL="${URL_NO_PROTOCOL#postgres://}"
    
    # Extract username and password
    USER_PASS="${URL_NO_PROTOCOL%%@*}"
    USERNAME="${USER_PASS%%:*}"
    PASSWORD="${USER_PASS##*:}"
    
    # Extract host, port, and database
    HOST_PORT_DB="${URL_NO_PROTOCOL##*@}"
    HOST_PORT="${HOST_PORT_DB%%/*}"
    DATABASE="${HOST_PORT_DB##*/}"
    HOST="${HOST_PORT%%:*}"
    PORT="${HOST_PORT##*:}"
    
    # Set environment variables for Spring Boot
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${HOST}:${PORT}/${DATABASE}"
    export SPRING_DATASOURCE_USERNAME="${USERNAME}"
    export SPRING_DATASOURCE_PASSWORD="${PASSWORD}"
    
    echo "Parsed DATABASE_URL into:"
    echo "  Host: $HOST"
    echo "  Port: $PORT"
    echo "  Database: $DATABASE"
    echo "  Username: $USERNAME"
    echo "  JDBC URL: jdbc:postgresql://${HOST}:${PORT}/${DATABASE}"
else
    echo "No DATABASE_URL found - using production profile PostgreSQL defaults"
fi

# Ensure PostgreSQL driver and dialect are set for production
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

echo "Starting application with production profile..."
echo "Active profile: $SPRING_PROFILES_ACTIVE"
echo "Port: ${PORT:-10000}"

# Start the application
java -Dspring.profiles.active=production -Dserver.port=${PORT:-10000} -jar app.jar
