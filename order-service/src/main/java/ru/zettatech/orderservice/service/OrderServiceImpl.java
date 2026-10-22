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
            productOrder.setOrderStatus("In progress");

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
            serviceResponse.setMessage("Order Placed Successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error creating order", e);
            serviceResponse.setCode("500");
            serviceResponse.setMessage("Internal Server Error");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
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
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            prodOrder.setOrderStatus("Cancelled");
            productOrderRepository.save(prodOrder);
            response.setCode("200");
            response.setMessage("Order Cancelled");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error cancelling order", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllOrders(GetAllOrderRequest request) {
        try {
            List<ProductOrder> allOrder = productOrderRepository.findByCartId(request.getCartId());

            if (!allOrder.isEmpty()) {
                GetAllOrderResponse response = new GetAllOrderResponse();
                List<Order> orders = new ArrayList<>();

                for (ProductOrder orderEntity : allOrder) {
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
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error getting orders", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
}
