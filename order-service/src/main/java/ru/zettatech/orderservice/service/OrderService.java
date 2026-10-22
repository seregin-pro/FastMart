package ru.zettatech.orderservice.service;

import org.springframework.http.ResponseEntity;
import ru.zettatech.orderservice.dto.request.CancelOrderRequest;
import ru.zettatech.orderservice.dto.request.CreateOrderRequest;
import ru.zettatech.orderservice.dto.request.GetAllOrderRequest;

import java.util.Optional;

public interface OrderService {
    ResponseEntity<?> createOrder(CreateOrderRequest request);
    ResponseEntity<?> cancelOrder(CancelOrderRequest request);
    ResponseEntity<?> getAllOrders(GetAllOrderRequest request);
    Optional<ru.zettatech.orderservice.entity.ProductOrder> findById(Long orderId);
    ResponseEntity<?> updateOrderStatus(Long orderId, String status);
}
