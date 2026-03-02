package ru.zettatech.customerservice.request.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {

	private Long customerId;
	private String customerName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	
}
