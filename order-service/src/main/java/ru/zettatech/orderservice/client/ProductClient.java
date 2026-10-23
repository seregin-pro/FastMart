package ru.zettatech.orderservice.client;

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

    public boolean reserveProduct(Long productId, Long quantity) {
        try {
            String url = productServiceUrl + "/api/products/" + productId + "/reserve";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("productId", productId);
            body.put("quantity", quantity);

            HttpEntity<java.util.Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Product service unavailable for reserve productId {}: {}", productId, e.getMessage());
            return false;
        }
    }

    public ProductInfo getProductInfo(Long productId) {
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
