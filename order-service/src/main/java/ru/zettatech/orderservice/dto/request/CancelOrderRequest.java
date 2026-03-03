package ru.zettatech.orderservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderRequest {

	private Long orderId;
	
}
