package ru.zettatech.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ProductApplication {
    static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}