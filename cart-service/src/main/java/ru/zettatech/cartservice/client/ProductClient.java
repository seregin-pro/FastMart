package ru.zettatech.cartservice.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${product-service.url:http://localhost:8082}")
    private String productServiceUrl;

    public ProductInfo getProductById(Long productId) {
        try {
            String url = productServiceUrl + "/api/products/" + productId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> product = response.getBody();
                ProductInfo info = new ProductInfo();
                info.setId((Long) product.get("id"));
                info.setName((String) product.get("name"));
                info.setPrice(new BigDecimal(product.get("price").toString()));
                info.setImage((String) product.get("image"));
                return info;
            }
            return null;
        } catch (RestClientException e) {
            log.warn("Product service unavailable for productId {}: {}", productId, e.getMessage());
            return null;
        }
    }

    public boolean productExists(Long productId) {
        try {
            String url = productServiceUrl + "/api/products/" + productId;
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Product service unavailable for existence check productId {}: {}", productId, e.getMessage());
            return false;
        }
    }

    @Getter
    @Setter
    public static class ProductInfo {
        private Long id;
        private String name;
        private BigDecimal price;
        private String image;
    }
}
