package ru.zettatech.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.zettatech.customerservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.customerservice.request.dto.CreateCustomerRequest;
import ru.zettatech.customerservice.request.dto.CustomerInfoRequest;
import ru.zettatech.customerservice.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/create-customer")
	public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequest request) {
		ResponseEntity<?> response = customerService.createCustomer(request);
		return response;
	}

	@PostMapping("/get-customer-info")
	public ResponseEntity<?> getCustomerInfo(@RequestBody CustomerInfoRequest request) {
		ResponseEntity<?> response = customerService.getCustomerInfo(request);
		return response;
	}

	@PostMapping("/create-update-address")
	public ResponseEntity<?> createUpdateAddress(@RequestBody CreateUpdateAddressRequest request){
		ResponseEntity<?> response = customerService.createUpdateAddress(request);
		return response;
	}
	
}
