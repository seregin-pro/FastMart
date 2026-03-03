package ru.zettatech.orderservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
	
	private Long cartId;
	private BigDecimal totalOrderPrice;
	private List<OrderProductItem> orderItems;

}
