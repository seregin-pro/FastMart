plugins {
    java
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.zettatech"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    // Spring Cloud Gateway (WebFlux-based)
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    // Load balancer (replaces Ribbon)
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    // Redis for rate limiting
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    // JWT validation
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webapp")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.test {
    useJUnitPlatform()
}
