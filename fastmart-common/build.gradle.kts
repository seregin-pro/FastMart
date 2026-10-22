plugins {
    java
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
}

dependencies {
    // Spring Boot Web for @ControllerAdvice, ResponseEntity, etc.
    implementation("org.springframework.boot:spring-boot-starter-webmvc:4.0.2")
    // Validation API
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("org.springframework.boot:spring-boot-starter-validation:4.0.2")
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    // SLF4J for logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    // Jackson for JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    // Servlet API для Filter
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.2")
}

tasks.test {
    useJUnitPlatform()
}
