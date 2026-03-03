package ru.zettatech.cartservice.request.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItem {
	private Long productId;
	private String name;
	private Long quantity;
	private BigDecimal pricePerUnit;
	private String image;
}
