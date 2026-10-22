package ru.zettatech.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zettatech.authservice.request.dto.SignInRequest;
import ru.zettatech.authservice.request.dto.SignUpRequest;
import ru.zettatech.authservice.response.dto.JWTTokenResponse;
import ru.zettatech.authservice.security.JWTTokenProvider;
import ru.zettatech.authservice.service.UserService;
import ru.zettatech.common.util.ValidationUtil;

import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JWTTokenProvider tokenProvider;

    @Value("${app.jwt.token.prefix}")
    private String tokenPrefix;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request, BindingResult result) {
        ResponseEntity<?> requestErrors = ValidationUtil.validateRequest(result);
        if (requestErrors != null) {
            return requestErrors;
        }
        return userService.signUp(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest request, BindingResult result) throws UnsupportedEncodingException {
        ResponseEntity<?> requestErrors = ValidationUtil.validateRequest(result);
        if (requestErrors != null) {
            return requestErrors;
        }

        return userService.authenticate(request.getUsername(), request.getPassword(), tokenPrefix);
    }
}
