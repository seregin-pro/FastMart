package ru.zettatech.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zettatech.authservice.request.dto.AddRoleRequest;
import ru.zettatech.authservice.service.AdminService;
import ru.zettatech.common.util.ValidationUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add_role")
    public ResponseEntity<?> addRole(@Valid @RequestBody AddRoleRequest request, BindingResult result) {
        ResponseEntity<?> requestErrors = ValidationUtil.validateRequest(result);
        if (requestErrors != null) {
            return requestErrors;
        }
        return adminService.addRole(request);
    }
}
