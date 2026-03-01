# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy gradle files
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY service/build.gradle.kts ./service/

# Download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy source
COPY service/src ./service/src

# Build
RUN ./gradlew :service:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/service/build/libs/*.jar app.jar

# SQLite database will be mounted
VOLUME /app/data

ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/app/data/etk.sqlite
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
