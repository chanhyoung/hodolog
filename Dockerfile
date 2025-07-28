# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy the Maven project file
COPY pom.xml ./

# Download dependencies (this will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application, skipping tests for a faster build
RUN mvn package -DskipTests -B

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Create a non-root user and group
RUN addgroup -S spring && adduser -S spring -G spring


# Copy the JAR file from the builder stage (fixed reference)
COPY --from=builder /app/target/*.jar /app/hodolog.jar

RUN mkdir -p /app/logs && chown -R spring:spring /app/logs

# Change ownership of the JAR file to the spring user
RUN chown spring:spring /app/hodolog.jar

# Switch to the non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/hodolog.jar"]