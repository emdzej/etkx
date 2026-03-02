# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy gradle wrapper and config
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle ./
COPY service/build.gradle ./service/

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source
COPY service/src ./service/src

# Build
RUN ./gradlew :service:bootJar --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/service/build/libs/*.jar app.jar

# SQLite database will be mounted
VOLUME /app/data

ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/etk.sqlite
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
