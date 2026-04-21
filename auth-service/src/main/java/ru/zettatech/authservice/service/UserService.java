package ru.zettatech.authservice.service;

import ru.zettatech.authservice.request.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<?> signUp(SignUpRequest request);
	
}
