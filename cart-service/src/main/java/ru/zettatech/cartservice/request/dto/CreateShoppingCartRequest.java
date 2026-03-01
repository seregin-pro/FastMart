package ru.zettatech.cartservice.request.dto;

//import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShoppingCartRequest {

//	@NotBlank(message = "userId is required to create user shopping cart")
	private Long userId;
	
}
