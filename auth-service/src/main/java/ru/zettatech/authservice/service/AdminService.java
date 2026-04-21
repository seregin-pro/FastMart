package ru.zettatech.authservice.service;

import ru.zettatech.authservice.request.dto.AddRoleRequest;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

public interface AdminService {

	ResponseEntity<?> addRole(@Valid AddRoleRequest request);

}
