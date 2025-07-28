# Stage 1: Build the application
FROM maven:3.8-openjdk-11 AS build

WORKDIR /app

# Copy the Maven project file
COPY pom.xml ./

# Download dependencies (this will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application, skipping tests for a faster build
RUN mvn package -DskipTests -B

# Stage 2: Create the runtime image
FROM openjdk:11-jre-alpine

WORKDIR /app

# Create a non-root user and group
RUN addgroup -S spring && adduser -S spring -G spring

# Switch to the non-root user
USER spring:spring

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app/hodolog.jar

# Expose the application port
EXPOSE 8080

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/hodolog.jar"]
