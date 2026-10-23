package ru.zettatech.cartservice.client;

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
public class OrderClient {

    private final RestTemplate restTemplate;

    @Value("${order-service.url:http://localhost:8084}")
    private String orderServiceUrl;

    public boolean createOrderFromCart(Long cartId, java.math.BigDecimal totalOrderPrice, java.util.List<Map<String, Object>> orderItems) {
        try {
            String url = orderServiceUrl + "/api/orders";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("cartId", cartId);
            body.put("totalOrderPrice", totalOrderPrice.toString());
            body.put("orderItems", orderItems);

            HttpEntity<java.util.Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Order service unavailable for cartId {}: {}", cartId, e.getMessage());
            return false;
        }
    }
}
