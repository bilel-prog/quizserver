#!/bin/bash

# Render.com Environment Variable Setup
# This script will be executed by Render to set up PostgreSQL environment variables

# Set Spring Profile to production
export SPRING_PROFILES_ACTIVE=production

# Database configuration will be automatically provided by Render via DATABASE_URL
# Additional environment variables for explicit configuration
export DB_DRIVER=org.postgresql.Driver
export DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Start the application
java -Dspring.profiles.active=production -Dserver.port=${PORT:-10000} -jar app.jar
