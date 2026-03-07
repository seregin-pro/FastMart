package ru.zettatech.orderservice.service;

import ru.zettatech.orderservice.dto.request.CancelOrderRequest;
import ru.zettatech.orderservice.dto.request.CreateOrderRequest;
import ru.zettatech.orderservice.dto.request.GetAllOrderRequest;
import ru.zettatech.orderservice.dto.response.GetAllOrderResponse;
import ru.zettatech.orderservice.dto.response.Order;
import ru.zettatech.orderservice.dto.response.ProductItem;
import ru.zettatech.orderservice.dto.response.ServiceResponse;
import ru.zettatech.orderservice.entity.OrderItem;
import ru.zettatech.orderservice.entity.ProductOrder;
import ru.zettatech.orderservice.repository.ProductOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository productOrderRepository;
	
	@Override
	public ResponseEntity<?> createOrder(CreateOrderRequest request) {
		
		ResponseEntity<?> responseEntity;
		ServiceResponse serviceResponse = new ServiceResponse();
		
		try {
			ProductOrder productOrder = new ProductOrder();
			productOrder.setCartId(request.getCartId());
			productOrder.setOrderStatus("In progress");
			
			List<OrderItem> orderItems = new ArrayList<>();
			
			for(int i = 0; i < request.getOrderItems().size(); i++) {
				OrderItem orderItem = new OrderItem();
				orderItem.setProductId(request.getOrderItems().get(i).getProductId());
				orderItem.setProductName(request.getOrderItems().get(i).getProductName());
				orderItem.setProductQuantity(request.getOrderItems().get(i).getProductQuantity());
				orderItem.setPricePerUnit(request.getOrderItems().get(i).getPricePerUnit());
				orderItem.setProductImageUrl(request.getOrderItems().get(i).getProductImageUrl());
				orderItem.setTotalPrice(request.getOrderItems().get(i).getTotalPrice());
				orderItems.add(orderItem);
			}			
			productOrder.setOrderItems(orderItems);
			productOrder.setTotalOrderPrice(request.getTotalOrderPrice());
			productOrderRepository.save(productOrder);
			serviceResponse.setCode("200");
			serviceResponse.setMessage("Order Placed Successfully");
			responseEntity = new ResponseEntity<>(serviceResponse, HttpStatus.OK);
		} catch (Exception e) {
			serviceResponse.setCode("500");
			serviceResponse.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<>(serviceResponse, HttpStatus.OK);
		}
		
		
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> cancelOrder(CancelOrderRequest request) {
		
		ServiceResponse response = new ServiceResponse();
		ResponseEntity<?> responseEntity;
		
		try {
			ProductOrder prodOrder = productOrderRepository.findById(request.getOrderId()).orElse(null);
			prodOrder.setOrderStatus("Cancelled");
			productOrderRepository.save(prodOrder);
			response.setCode("200");
			response.setMessage("Order Cancelled");
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> getAllOrders(GetAllOrderRequest request) {

		ResponseEntity<?> responseEntity = null;
	
		try {
			
			List<ProductOrder> allOrder = productOrderRepository.findByCartId(request.getCartId());
			
			if(!(allOrder.size() == 0)) {
				GetAllOrderResponse response = new GetAllOrderResponse();	
				
				List<Order> orders = new ArrayList<>();
				
				for(int i = 0; i < allOrder.size(); i++){
					Order order = new Order();
					order.setOrderId(allOrder.get(i).getId());
					order.setTotalOrderPrice(allOrder.get(i).getTotalOrderPrice());
					order.setOrderStatus(allOrder.get(i).getOrderStatus());
					order.setCreatedAt(allOrder.get(i).getCreatedAt());
					order.setUpdatedAt(allOrder.get(i).getUpdatedAt());
					List<ProductItem> products = new ArrayList<>();
					for(int j = 0; j < allOrder.get(i).getOrderItems().size(); j++) {
						ProductItem item = new ProductItem();
						item.setId(allOrder.get(i).getOrderItems().get(j).getId());
						item.setProductId(allOrder.get(i).getOrderItems().get(j).getProductId());
						item.setProductName(allOrder.get(i).getOrderItems().get(j).getProductName());
						item.setProductQuantity(allOrder.get(i).getOrderItems().get(j).getProductQuantity());
						item.setTotalPrice(allOrder.get(i).getOrderItems().get(j).getTotalPrice());
						item.setPricePerUnit(allOrder.get(i).getOrderItems().get(j).getPricePerUnit());
						item.setProductImageUrl(allOrder.get(i).getOrderItems().get(j).getProductImageUrl());
						products.add(item);				
					}
					order.setOrderItems(products);
					orders.add(order);
				}
				response.setOrders(orders);
				responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
			}else {
				responseEntity = new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
			}
			
		} catch (Exception e) {
			log.error("No orders");
		}		
		return responseEntity;
	}

}
