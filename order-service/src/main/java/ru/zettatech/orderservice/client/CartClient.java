package ru.zettatech.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartClient {

    private final RestTemplate restTemplate;

    @Value("${cart-service.url:http://localhost:8083}")
    private String cartServiceUrl;

    public boolean cleanCart(Long cartId) {
        try {
            String url = cartServiceUrl + "/api/cart/" + cartId + "/clean";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("cartId", cartId);
            body.put("productIds", new java.util.ArrayList<Long>());

            HttpEntity<java.util.Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Cart service unavailable for cartId {}: {}", cartId, e.getMessage());
            return false;
        }
    }

    public boolean cartExists(Long cartId) {
        try {
            String url = cartServiceUrl + "/api/cart/" + cartId;
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.GET, null, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Cart service unavailable for cartId {}: {}", cartId, e.getMessage());
            return false;
        }
    }
}
