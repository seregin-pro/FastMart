package ru.zettatech.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.authservice.entity.Address;
import ru.zettatech.authservice.entity.User;
import ru.zettatech.authservice.request.dto.CreateUserRequest;
import ru.zettatech.authservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.authservice.request.dto.UserInfoRequest;
import ru.zettatech.authservice.response.dto.ServiceResponse;
import ru.zettatech.authservice.response.dto.UserInfoResponse;
import ru.zettatech.authservice.service.UserService;
import ru.zettatech.common.util.ValidationUtil;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request, BindingResult result) {
        ResponseEntity<?> requestErrors = ValidationUtil.validateRequest(result);
        if (requestErrors != null) {
            return requestErrors;
        }
        return userService.createUser(request);
    }

    @PostMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestBody UserInfoRequest request) {
        return userService.getUserInfo(request);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("X-User-Name") String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            @RequestHeader("X-User-Name") String username,
            @RequestBody CreateUserRequest request) {
        return userService.updateUserByUsername(username, request);
    }

    @PostMapping("/address")
    public ResponseEntity<?> createUpdateAddress(@RequestBody CreateUpdateAddressRequest request) {
        return userService.createUpdateAddress(request);
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<?> getAddresses(@RequestHeader("X-User-Name") String username) {
        return userService.getAddressesByUsername(username);
    }

    @PutMapping("/me/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(
            @RequestHeader("X-User-Name") String username,
            @PathVariable Long addressId,
            @RequestBody CreateUpdateAddressRequest request) {
        return userService.updateAddress(username, addressId, request);
    }

    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @RequestHeader("X-User-Name") String username,
            @PathVariable Long addressId) {
        return userService.deleteAddress(username, addressId);
    }
}
