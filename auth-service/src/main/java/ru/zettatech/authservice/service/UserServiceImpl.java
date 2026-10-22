package ru.zettatech.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zettatech.authservice.entity.Address;
import ru.zettatech.authservice.entity.Role;
import ru.zettatech.authservice.entity.User;
import ru.zettatech.authservice.model.UserPrincipal;
import ru.zettatech.authservice.repository.AddressRepository;
import ru.zettatech.authservice.repository.RoleRepository;
import ru.zettatech.authservice.repository.UserRepository;
import ru.zettatech.authservice.request.dto.CreateUserRequest;
import ru.zettatech.authservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.authservice.request.dto.SignUpRequest;
import ru.zettatech.authservice.request.dto.UserInfoRequest;
import ru.zettatech.authservice.response.dto.JWTTokenResponse;
import ru.zettatech.authservice.response.dto.ServiceResponse;
import ru.zettatech.authservice.response.dto.SignUpResponse;
import ru.zettatech.authservice.response.dto.UserInfoResponse;
import ru.zettatech.authservice.security.JWTTokenProvider;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider tokenProvider;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserPrincipal.build(user);
    }

    @Override
    public ResponseEntity<?> authenticate(String username, String password, String tokenPrefix) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = tokenPrefix + " " + tokenProvider.generateToken(authentication);

            JWTTokenResponse response = new JWTTokenResponse();
            response.setToken(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("401");
            res.setMessage("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> signUp(SignUpRequest request) {
        ServiceResponse serviceResponse = new ServiceResponse();
        SignUpResponse signUpResponse = new SignUpResponse();

        try {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                serviceResponse.setCode("400");
                serviceResponse.setMessage("Passwords do not match");
                return ResponseEntity.ok(serviceResponse);
            }

            if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
                serviceResponse.setCode("400");
                serviceResponse.setMessage("Username already registered");
                return ResponseEntity.ok(serviceResponse);
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

            Role userRole = roleRepository.findRoleByName("ROLE_USER");
            if (userRole != null) {
                userRepository.save(user);
                user.addRole(userRole);
                User registeredUser = userRepository.save(user);

                signUpResponse.setUserId(registeredUser.getId());
                signUpResponse.setCode("200");
                signUpResponse.setMessage("User registered successfully");
                return ResponseEntity.ok(signUpResponse);
            }

            serviceResponse.setCode("401");
            serviceResponse.setMessage("Registration failed");
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception e) {
            log.error("Error during signup", e);
            serviceResponse.setCode("500");
            serviceResponse.setMessage("Internal server error");
            return ResponseEntity.internalServerError().body(serviceResponse);
        }
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserRequest request) {
        return ResponseEntity.ok(new ServiceResponse());
    }

    @Override
    public ResponseEntity<?> getUserInfo(UserInfoRequest request) {
        return ResponseEntity.ok(new UserInfoResponse());
    }

    @Override
    public ResponseEntity<?> getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if (userOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("User not found");
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        UserInfoResponse response = new UserInfoResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        if (user.getAddress() != null) {
            ru.zettatech.authservice.response.dto.Address addr = new ru.zettatech.authservice.response.dto.Address();
            addr.setAddressId(user.getAddress().getAddressId());
            addr.setAddressLine(user.getAddress().getAddressLine());
            addr.setCity(user.getAddress().getCity());
            addr.setPin(user.getAddress().getPin());
            addr.setState(user.getAddress().getState());
            addr.setCountry(user.getAddress().getCountry());
            response.setAddress(addr);
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateUserByUsername(String username, CreateUserRequest request) {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if (userOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("User not found");
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        userRepository.save(user);
        ServiceResponse res = new ServiceResponse();
        res.setCode("200");
        res.setMessage("User updated successfully");
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request) {
        Optional<User> userOpt = userRepository.findUserByUsername(request.getUserName());
        if (userOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("User not found");
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Address address = user.getAddress() != null ? user.getAddress() : new Address();

        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPin(request.getPin());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setUser(user);

        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);

        ServiceResponse res = new ServiceResponse();
        res.setCode("200");
        res.setMessage("Address saved successfully");
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<?> getAddressesByUsername(String username) {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if (userOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("User not found");
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Address address = user.getAddress();
        if (address == null) {
            return ResponseEntity.ok(new ServiceResponse());
        }

        return ResponseEntity.ok(address);
    }

    @Override
    public ResponseEntity<?> updateAddress(String username, Long addressId, CreateUpdateAddressRequest request) {
        Optional<User> userOpt = userRepository.findUserByUsername(username);
        if (userOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("User not found");
            return ResponseEntity.notFound().build();
        }

        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("Address not found");
            return ResponseEntity.notFound().build();
        }

        Address address = addressOpt.get();
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setPin(request.getPin());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        addressRepository.save(address);

        ServiceResponse res = new ServiceResponse();
        res.setCode("200");
        res.setMessage("Address updated");
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<?> deleteAddress(String username, Long addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            ServiceResponse res = new ServiceResponse();
            res.setCode("404");
            res.setMessage("Address not found");
            return ResponseEntity.notFound().build();
        }

        addressRepository.deleteById(addressId);
        ServiceResponse res = new ServiceResponse();
        res.setCode("200");
        res.setMessage("Address deleted");
        return ResponseEntity.ok(res);
    }
}
