#!/bin/bash

# Render.com Environment Variable Setup
# This script will be executed by Render to set up PostgreSQL environment variables

# Set Spring Profile to production
export SPRING_PROFILES_ACTIVE=production

# Check if DATABASE_URL exists and debug
echo "DATABASE_URL environment variable: ${DATABASE_URL:-'NOT SET'}"
printenv | grep -i database || echo "No DATABASE environment variables found"

# If DATABASE_URL exists, convert it to proper JDBC format
if [ -n "$DATABASE_URL" ]; then
    echo "DATABASE_URL found - converting to JDBC format"
    
    # Convert postgres:// to jdbc:postgresql:// if needed
    if [[ "$DATABASE_URL" == postgres://* ]]; then
        JDBC_DATABASE_URL="${DATABASE_URL/postgres:/jdbc:postgresql:}"
        echo "Converted postgres:// to jdbc:postgresql://"
    elif [[ "$DATABASE_URL" == postgresql://* ]]; then
        JDBC_DATABASE_URL="jdbc:${DATABASE_URL}"
        echo "Converted postgresql:// to jdbc:postgresql://"
    else
        JDBC_DATABASE_URL="$DATABASE_URL"
        echo "Using DATABASE_URL as-is"
    fi
    
    export SPRING_DATASOURCE_URL="$JDBC_DATABASE_URL"
    echo "Set SPRING_DATASOURCE_URL to: $JDBC_DATABASE_URL"
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
