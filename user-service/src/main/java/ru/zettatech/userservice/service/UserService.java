package ru.zettatech.userservice.service;

import org.springframework.http.ResponseEntity;

import ru.zettatech.userservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.userservice.request.dto.CreateUserRequest;
import ru.zettatech.userservice.request.dto.UserInfoRequest;

public interface UserService {

	ResponseEntity<?> createUser(CreateUserRequest request);

	ResponseEntity<?> getUserInfo(UserInfoRequest request);

	ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request);

}
