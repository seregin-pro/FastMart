package ru.zettatech.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zettatech.authservice.entity.Role;
import ru.zettatech.authservice.repository.RoleRepository;
import ru.zettatech.authservice.request.dto.AddRoleRequest;
import ru.zettatech.authservice.response.dto.ServiceResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> addRole(AddRoleRequest request) {
        ServiceResponse serviceResponse = new ServiceResponse();

        try {
            Role savedRole = roleRepository.findRoleByName(request.getRoleName());
            if (savedRole != null) {
                serviceResponse.setCode("409");
                serviceResponse.setMessage("A Role with {" + request.getRoleName() + "} already existed");
                return new ResponseEntity<>(serviceResponse, HttpStatus.CONFLICT);
            }

            Role role = new Role();
            role.setName(request.getRoleName());
            roleRepository.save(role);
            serviceResponse.setCode("201");
            serviceResponse.setMessage("Role added successfully");
            return new ResponseEntity<>(serviceResponse, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("There is an error while adding role: {}", e.getMessage());
            serviceResponse.setCode("501");
            serviceResponse.setMessage("There is an error while adding role");
            return new ResponseEntity<>(serviceResponse, HttpStatus.CREATED);
        }
    }
}
