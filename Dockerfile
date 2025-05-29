FROM maven:3.9.9-eclipse-temurin-23-alpine AS builder

WORKDIR /app


COPY pom.xml .

# Download project dependencies. This step is cached if pom.xml doesn't change.
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Package the application into a JAR file
RUN mvn  install -DskipTests

# --- Stage 2: Create the final, lightweight runtime image ---
# Use a smaller JRE-only image for the final application
FROM openjdk:23-jdk-slim

# Set the working directory inside the final image
WORKDIR /app

# Copy the built JAR file from the 'builder' stage
# The 'target/*.jar' pattern will pick up the generated JAR (e.g., your-app-0.0.1-SNAPSHOT.jar)
COPY --from=builder /app/target/*.jar app.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
