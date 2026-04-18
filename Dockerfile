# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies (this caches them for faster subsequent builds)
RUN mvn dependency:go-offline
COPY src ./src
# Build the application, skipping tests to speed up the process
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the built jar file from the previous stage
COPY --from=build /app/target/*.jar app.jar
# Expose the port (Render provides the PORT environment variable)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
