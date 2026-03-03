package ru.zettatech.orderservice.service;

import ru.zettatech.orderservice.dto.request.CancelOrderRequest;
import ru.zettatech.orderservice.dto.request.CreateOrderRequest;
import ru.zettatech.orderservice.dto.request.GetAllOrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {

	ResponseEntity<?> createOrder(CreateOrderRequest request);

	ResponseEntity<?> cancelOrder(CancelOrderRequest request);

	ResponseEntity<?> getAllOrders(GetAllOrderRequest request);

}
