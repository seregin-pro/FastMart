package ru.zettatech.customerservice.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long customerId;
	private String customerName;
	private String firstName;
	private String lastName;
	private String email;

	@OneToOne
	private Address address;
	private Long cartId;
}
