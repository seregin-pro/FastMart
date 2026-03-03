package ru.zettatech.orderservice.controller;

import ru.zettatech.orderservice.dto.request.CancelOrderRequest;
import ru.zettatech.orderservice.dto.request.CreateOrderRequest;
import ru.zettatech.orderservice.dto.request.GetAllOrderRequest;
import ru.zettatech.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/get_all_orders")
	public ResponseEntity<?> getAllOrders(@RequestBody GetAllOrderRequest request){
		ResponseEntity<?> response = orderService.getAllOrders(request);
		return response;
	}
	
	@PostMapping("/create_order")
	public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request){
		
		ResponseEntity<?> response = orderService.createOrder(request);
		
		return response;
		
	}
	
	@PostMapping("/cancel_order")
	public ResponseEntity<?> cancelOrder(@RequestBody CancelOrderRequest request){
		ResponseEntity<?> response = orderService.cancelOrder(request);
		return response;
	}
	
}
