package ru.zettatech.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zettatech.orderservice.dto.request.CancelOrderRequest;
import ru.zettatech.orderservice.dto.request.CreateOrderRequest;
import ru.zettatech.orderservice.dto.request.GetAllOrderRequest;
import ru.zettatech.orderservice.dto.request.OrderProductItem;
import ru.zettatech.orderservice.dto.response.GetAllOrderResponse;
import ru.zettatech.orderservice.dto.response.Order;
import ru.zettatech.orderservice.dto.response.ProductItem;
import ru.zettatech.orderservice.dto.response.ServiceResponse;
import ru.zettatech.orderservice.entity.OrderItem;
import ru.zettatech.orderservice.entity.ProductOrder;
import ru.zettatech.orderservice.repository.ProductOrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ProductOrderRepository productOrderRepository;

    @Override
    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
        ServiceResponse serviceResponse = new ServiceResponse();

        try {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setCartId(request.getCartId());
            productOrder.setOrderStatus("PENDING");

            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderProductItem item : request.getOrderItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(item.getProductId());
                orderItem.setProductName(item.getProductName());
                orderItem.setProductQuantity(item.getProductQuantity());
                orderItem.setPricePerUnit(item.getPricePerUnit());
                orderItem.setProductImageUrl(item.getProductImageUrl());
                orderItem.setTotalPrice(item.getTotalPrice());
                orderItems.add(orderItem);
            }

            productOrder.setOrderItems(orderItems);
            productOrder.setTotalOrderPrice(request.getTotalOrderPrice());
            productOrderRepository.save(productOrder);

            serviceResponse.setCode("200");
            serviceResponse.setMessage("Order placed successfully");
            return ResponseEntity.ok(serviceResponse);
        } catch (Exception e) {
            log.error("Error creating order", e);
            serviceResponse.setCode("500");
            serviceResponse.setMessage("Internal Server Error");
            return ResponseEntity.internalServerError().body(serviceResponse);
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(CancelOrderRequest request) {
        ServiceResponse response = new ServiceResponse();

        try {
            ProductOrder prodOrder = productOrderRepository.findById(request.getOrderId()).orElse(null);
            if (prodOrder == null) {
                response.setCode("404");
                response.setMessage("Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            prodOrder.setOrderStatus("CANCELLED");
            productOrderRepository.save(prodOrder);
            response.setCode("200");
            response.setMessage("Order cancelled");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error cancelling order", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Override
    public ResponseEntity<?> getAllOrders(GetAllOrderRequest request) {
        try {
            List<ProductOrder> allOrders = productOrderRepository.findByCartId(request.getCartId());

            if (!allOrders.isEmpty()) {
                GetAllOrderResponse response = new GetAllOrderResponse();
                List<Order> orders = new ArrayList<>();

                for (ProductOrder orderEntity : allOrders) {
                    Order order = new Order();
                    order.setOrderId(orderEntity.getId());
                    order.setTotalOrderPrice(orderEntity.getTotalOrderPrice());
                    order.setOrderStatus(orderEntity.getOrderStatus());
                    order.setCreatedAt(orderEntity.getCreatedAt());
                    order.setUpdatedAt(orderEntity.getUpdatedAt());

                    List<ProductItem> products = new ArrayList<>();
                    for (OrderItem item : orderEntity.getOrderItems()) {
                        ProductItem productItem = new ProductItem();
                        productItem.setId(item.getId());
                        productItem.setProductId(item.getProductId());
                        productItem.setProductName(item.getProductName());
                        productItem.setProductQuantity(item.getProductQuantity());
                        productItem.setTotalPrice(item.getTotalPrice());
                        productItem.setPricePerUnit(item.getPricePerUnit());
                        productItem.setProductImageUrl(item.getProductImageUrl());
                        products.add(productItem);
                    }
                    order.setOrderItems(products);
                    orders.add(order);
                }
                response.setOrders(orders);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }
        } catch (Exception e) {
            log.error("Error getting orders", e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    public Optional<ProductOrder> findById(Long orderId) {
        return productOrderRepository.findById(orderId);
    }

    public List<ProductOrder> findAllByUserId(Long userId) {
        // TODO: Add userId to ProductOrder entity for proper user-based queries
        return productOrderRepository.findAll();
    }

    public ResponseEntity<?> updateOrderStatus(Long orderId, String status) {
        ServiceResponse response = new ServiceResponse();

        try {
            ProductOrder order = productOrderRepository.findById(orderId).orElse(null);
            if (order == null) {
                response.setCode("404");
                response.setMessage("Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            order.setOrderStatus(status);
            productOrderRepository.save(order);
            response.setCode("200");
            response.setMessage("Order status updated to " + status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating order status", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
