# Этап 1: Сборка
FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /build

# Копируем файлы конфигурации Gradle
COPY gradle ./gradle
COPY gradlew ./gradlew
COPY settings.gradle.kts ./settings.gradle.kts
COPY build.gradle.kts ./build.gradle.kts

# Копируем только исходный код auth-service
COPY auth-service/src ./auth-service/src

# Собираем JAR файл
RUN ./gradlew :auth-service:build -x test --no-daemon

# Этап 2: Выполнение
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Копируем JAR файл из слоя сборки
COPY --from=build /build/auth-service/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
