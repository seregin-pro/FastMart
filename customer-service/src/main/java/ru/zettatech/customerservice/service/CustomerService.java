package ru.zettatech.customerservice.service;

import org.springframework.http.ResponseEntity;

import ru.zettatech.customerservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.customerservice.request.dto.CreateCustomerRequest;
import ru.zettatech.customerservice.request.dto.CustomerInfoRequest;

public interface CustomerService {

	ResponseEntity<?> createCustomer(CreateCustomerRequest request);

	ResponseEntity<?> getCustomerInfo(CustomerInfoRequest request);

	ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request);

}
