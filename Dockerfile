# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the jar file from target directory
COPY target/DriveOrbit-core-0.0.1-SNAPSHOT.jar app.jar

# Copy Firebase credentials
COPY src/main/resources/google-services.json /app/google-services.json

# Create directory for QR code uploads
RUN mkdir -p /app/upload/qrcodes

# Expose the application port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
