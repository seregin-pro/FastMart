# Build stage
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Gradle wrapper
COPY gradlew .
COPY gradle ./gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY fastmart-common/build.gradle.kts ./fastmart-common/
COPY product-service/build.gradle.kts ./product-service/
COPY cart-service/build.gradle.kts ./cart-service/
COPY order-service/build.gradle.kts ./order-service/
COPY auth-service/build.gradle.kts ./auth-service/

# Copy source code
COPY fastmart-common/src ./fastmart-common/src
COPY product-service/src ./product-service/src
COPY cart-service/src ./cart-service/src
COPY order-service/src ./order-service/src
COPY auth-service/src ./auth-service/src

# Build
RUN chmod +x gradlew && \
    ./gradlew :product-service:build -x test --no-daemon

# Run stage
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/product-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
