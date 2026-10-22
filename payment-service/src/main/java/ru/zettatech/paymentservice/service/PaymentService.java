package ru.zettatech.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PaymentService {

    public ResponseEntity<?> createPayment(Map<String, Object> request) {
        // TODO: Реальная интеграция с ЮKassa
        // Для тестирования возвращаем mock-ответ
        Map<String, Object> response = Map.of(
                "id", "pay_" + System.currentTimeMillis(),
                "status", "pending",
                "confirmation", Map.of(
                        "type", "redirect",
                        "confirmation_url", "http://localhost:8085/api/payments/confirm/test"
                ),
                "amount", request.get("amount"),
                "order_id", request.get("orderId")
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> handleWebhook(Map<String, Object> payload) {
        log.info("Received YooKassa webhook: {}", payload);
        // TODO: Реальная обработка webhook от ЮKassa
        // 1. Verify signature
        // 2. Update order status based on event type
        // 3. Return 200 OK
        return ResponseEntity.ok(Map.of("status", "received"));
    }

    public ResponseEntity<?> getPaymentStatus(String orderId) {
        // TODO: Реальный запрос к ЮKassa API
        Map<String, Object> response = Map.of(
                "order_id", orderId,
                "status", "pending",
                "amount", "0.00"
        );
        return ResponseEntity.ok(response);
    }
}
