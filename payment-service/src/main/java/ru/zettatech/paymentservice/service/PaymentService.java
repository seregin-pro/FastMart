package ru.zettatech.paymentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zettatech.paymentservice.client.OrderClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final RestTemplate restTemplate;
    private final OrderClient orderClient;

    @Value("${yookassa.api-key:test-api-key}")
    private String yookassaApiKey;

    @Value("${yookassa.store-id:123456}")
    private String yookassaStoreId;

    @Value("${yookassa.environment:test}")
    private String yookassaEnvironment;

    private static final String YOOKASSA_API_URL = "https://api.yookassa.ru/v3/payments";

    public ResponseEntity<?> createPayment(Map<String, Object> request) {
        try {
            String orderId = (String) request.get("orderId");
            Object amountObj = request.get("amount");
            double amount = amountObj instanceof Number ? ((Number) amountObj).doubleValue() : Double.parseDouble(amountObj.toString());

            // Build YooKassa request
            Map<String, Object> yookassaRequest = Map.of(
                    "amount", Map.of(
                            "value", String.format("%.2f", amount),
                            "currency", "RUB"
                    ),
                    "confirmation", Map.of(
                            "type", "redirect",
                            "return_url", "http://localhost:3000/payment/success?orderId=" + orderId
                    ),
                    "capture", false,
                    "description", "Order #" + orderId + " - FastMart"
            );

            // Build auth header (Base64 of storeId:secretKey)
            String auth = Base64.getEncoder().encodeToString(
                    (yookassaStoreId + ":" + yookassaApiKey).getBytes(StandardCharsets.UTF_8)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + auth);
            headers.set("Idempotence-Key", "req_" + System.currentTimeMillis());

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(yookassaRequest, headers);

            // Make request to YooKassa
            ResponseEntity<Map> response = restTemplate.exchange(
                    YOOKASSA_API_URL, HttpMethod.POST, httpEntity, Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> yookassaResponse = response.getBody();
                String paymentId = (String) yookassaResponse.get("id");
                String status = (String) yookassaResponse.get("status");
                String confirmationUrl = "";

                if (yookassaResponse.containsKey("confirmation")) {
                    Map<String, Object> confirmation = (Map<String, Object>) yookassaResponse.get("confirmation");
                    confirmationUrl = (String) confirmation.get("confirmation_url");
                }

                // Update order status to PAID (pending payment)
                orderClient.updateOrderStatus(orderId, "PAID");

                return ResponseEntity.ok(Map.of(
                        "yookassaPaymentId", paymentId,
                        "status", status,
                        "confirmationUrl", confirmationUrl,
                        "orderId", orderId
                ));
            }

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            log.error("Error creating YooKassa payment", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Payment creation failed",
                    "message", e.getMessage()
            ));
        }
    }

    public ResponseEntity<?> handleWebhook(Map<String, Object> payload) {
        try {
            String eventType = (String) payload.get("event");

            // Process payment events
            if ("payment.succeeded".equals(eventType)) {
                Map<String, Object> objectObj = (Map<String, Object>) payload.get("object");
                String paymentId = (String) objectObj.get("id");
                log.info("Payment succeeded: {}", paymentId);

                // TODO: Update order status to CONFIRMED
                // orderClient.updateOrderStatus(orderId, "CONFIRMED");
            } else if ("payment.canceled".equals(eventType)) {
                log.info("Payment canceled");
                // TODO: Update order status to CANCELLED
            }

            return ResponseEntity.ok(Map.of("status", "received"));
        } catch (Exception e) {
            log.error("Error processing YooKassa webhook", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Webhook processing failed"));
        }
    }

    public ResponseEntity<?> getPaymentStatus(String orderId) {
        try {
            Map<String, Object> orderStatus = orderClient.getOrderStatus(orderId);
            if (orderStatus != null) {
                return ResponseEntity.ok(orderStatus);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error getting payment status for order {}", orderId, e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Payment status check failed",
                    "message", e.getMessage()
            ));
        }
    }
}
