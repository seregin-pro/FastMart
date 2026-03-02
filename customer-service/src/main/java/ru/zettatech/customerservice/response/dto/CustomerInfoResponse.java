package ru.zettatech.customerservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInfoResponse {

	private Long customerId;
	private String customerName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	
}
