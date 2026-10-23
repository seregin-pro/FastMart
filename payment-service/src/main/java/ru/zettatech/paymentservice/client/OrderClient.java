package ru.zettatech.paymentservice.client;

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

    public boolean updateOrderStatus(String orderId, String status) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId + "/status?status=" + status;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, null, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Order service unavailable for orderId {}: {}", orderId, e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getOrderStatus(String orderId) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            return null;
        } catch (RestClientException e) {
            log.warn("Order service unavailable for orderId {}: {}", orderId, e.getMessage());
            return null;
        }
    }
}
