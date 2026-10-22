package ru.zettatech.authservice.service;

import org.springframework.http.ResponseEntity;
import ru.zettatech.authservice.request.dto.CreateUserRequest;
import ru.zettatech.authservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.authservice.request.dto.SignUpRequest;
import ru.zettatech.authservice.request.dto.UserInfoRequest;

public interface UserService {
    ResponseEntity<?> signUp(SignUpRequest request);
    ResponseEntity<?> authenticate(String username, String password, String tokenPrefix);
    ResponseEntity<?> createUser(CreateUserRequest request);
    ResponseEntity<?> getUserInfo(UserInfoRequest request);
    ResponseEntity<?> getUserByUsername(String username);
    ResponseEntity<?> updateUserByUsername(String username, CreateUserRequest request);
    ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request);
    ResponseEntity<?> getAddressesByUsername(String username);
    ResponseEntity<?> updateAddress(String username, Long addressId, CreateUpdateAddressRequest request);
    ResponseEntity<?> deleteAddress(String username, Long addressId);
}
