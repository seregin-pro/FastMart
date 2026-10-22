package ru.zettatech.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zettatech.userservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.userservice.request.dto.CreateUserRequest;
import ru.zettatech.userservice.request.dto.UserInfoRequest;
import ru.zettatech.userservice.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(@RequestBody UserInfoRequest request) {
        return userService.getUserInfo(request);
    }

    @PostMapping("/create-update-address")
    public ResponseEntity<?> createUpdateAddress(@RequestBody CreateUpdateAddressRequest request) {
        return userService.createUpdateAddress(request);
    }
}
