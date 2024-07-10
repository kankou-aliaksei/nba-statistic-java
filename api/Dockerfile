# Use Amazon Corretto 21 as the base image
FROM amazoncorretto:21-alpine as builder

# Set the working directory for the build
WORKDIR /app

# Copy the Gradle wrapper and build scripts
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Grant execute permissions for the Gradle wrapper
RUN chmod +x gradlew

# Build the application
RUN ./gradlew build

# Use Amazon Corretto 21 as the base image for the runtime
FROM amazoncorretto:21-alpine

# Set the working directory for the runtime
WORKDIR /app

# Copy the build jar file from the builder stage
COPY --from=builder /app/build/libs/nba-statistic-api-0.0.1.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
