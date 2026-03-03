package ru.zettatech.orderservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order {

	private Long orderId;
	private BigDecimal totalOrderPrice;
	private String orderStatus;
	List<ProductItem> orderItems;
	private Date createdAt;
	private Date updatedAt;
	
}
