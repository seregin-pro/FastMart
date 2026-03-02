package ru.zettatech.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ru.zettatech.customerservice.entity.Address;
import ru.zettatech.customerservice.entity.Customer;
import ru.zettatech.customerservice.repository.CustomerRepository;
import ru.zettatech.customerservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.customerservice.request.dto.CreateCustomerRequest;
import ru.zettatech.customerservice.request.dto.CustomerInfoRequest;
import ru.zettatech.customerservice.response.dto.ServiceResponse;
import ru.zettatech.customerservice.response.dto.CustomerInfoResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public ResponseEntity<?> createCustomer(CreateCustomerRequest request) {

		Customer customer = new Customer();
		Address address = new Address();

		ResponseEntity<?> responseEntity = null;
		ServiceResponse response = new ServiceResponse();

		try {
			customer.setCustomerId(request.getCustomerId());
			customer.setCustomerName(request.getCustomerName());
			customer.setFirstName(request.getFirstName());
			customer.setLastName(request.getLastName());
			customer.setEmail(request.getEmail());
			customer.setCartId(request.getCartId());

			if(!(request.getAddress() == null)) {
				address.setAddressLine(request.getAddress().getAddressLine());
				address.setCity(request.getAddress().getCity());
				address.setPin(request.getAddress().getPin());
				address.setState(request.getAddress().getState());
				address.setCountry(request.getAddress().getCountry());
				customer.setAddress(address);
			}			
			
			customerRepository.save(customer);
			response.setCode("200");
			response.setMessage("Customer Data Persisted");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

	@Override
	public ResponseEntity<?> getCustomerInfo(CustomerInfoRequest request) {

		ResponseEntity<?> responseEntity = null;
		ServiceResponse response = new ServiceResponse();
		try {
			Customer customer = customerRepository.findByCustomerId(request.getCustomerId()).orElse(null);
			if (!(customer == null)) {
				CustomerInfoResponse customerInfo = new CustomerInfoResponse();
				customerInfo.setCustomerId(customer.getCustomerId());
				customerInfo.setCustomerName(customer.getCustomerName());
				customerInfo.setFirstName(customer.getFirstName());
				customerInfo.setLastName(customer.getLastName());
				customerInfo.setEmail(customer.getEmail());				

				if(!(customerInfo.getAddress() == null)) {
					ru.zettatech.customerservice.response.dto.Address address = new ru.zettatech.customerservice.response.dto.Address();
					address.setAddressId(customer.getAddress().getAddressId());
					address.setAddressLine(customer.getAddress().getAddressLine());
					address.setCity(customer.getAddress().getCity());
					address.setPin(customer.getAddress().getPin());
					address.setState(customer.getAddress().getState());
					address.setCountry(customer.getAddress().getCountry());
					customerInfo.setAddress(address);
				}				

				customerInfo.setCartId(customer.getCartId());

				responseEntity = new ResponseEntity<CustomerInfoResponse>(customerInfo, HttpStatus.OK);

			} else {
				response.setCode("404");
				response.setMessage("customer information not found");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request) {

		ResponseEntity<?> responseEntity = null;
		ServiceResponse response = new ServiceResponse();

		try {
			Customer customer = customerRepository.findByCustomerId(request.getCustomerId()).orElse(null);

			if (!(customer == null)) {
				Address address = new Address();
				address.setAddressLine(request.getAddressLine());
				address.setCity(request.getCity());
				address.setPin(request.getPin());
				address.setState(request.getPin());
				address.setCountry(request.getCountry());
				customer.setAddress(address);
				customerRepository.save(customer);
				response.setCode("200");
				response.setMessage("Address information Synced");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

}
