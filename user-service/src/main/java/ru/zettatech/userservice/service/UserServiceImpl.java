package ru.zettatech.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zettatech.userservice.entity.Address;
import ru.zettatech.userservice.entity.User;
import ru.zettatech.userservice.repository.UserRepository;
import ru.zettatech.userservice.request.dto.CreateUpdateAddressRequest;
import ru.zettatech.userservice.request.dto.CreateUserRequest;
import ru.zettatech.userservice.request.dto.UserInfoRequest;
import ru.zettatech.userservice.response.dto.ServiceResponse;
import ru.zettatech.userservice.response.dto.UserInfoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> createUser(CreateUserRequest request) {
        User user = new User();
        Address address = new Address();
        ServiceResponse response = new ServiceResponse();

        try {
            user.setUserId(request.getUserId());
            user.setUserName(request.getUserName());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setCartId(request.getCartId());

            if (request.getAddress() != null) {
                address.setAddressLine(request.getAddress().getAddressLine());
                address.setCity(request.getAddress().getCity());
                address.setPin(request.getAddress().getPin());
                address.setState(request.getAddress().getState());
                address.setCountry(request.getAddress().getCountry());
                user.setAddress(address);
            }

            userRepository.save(user);
            response.setCode("200");
            response.setMessage("User Data Persisted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error creating user", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getUserInfo(UserInfoRequest request) {
        ServiceResponse response = new ServiceResponse();

        try {
            User user = userRepository.findByUserId(request.getUserId()).orElse(null);
            if (user != null) {
                UserInfoResponse userInfo = new UserInfoResponse();
                userInfo.setUserId(user.getUserId());
                userInfo.setUserName(user.getUserName());
                userInfo.setFirstName(user.getFirstName());
                userInfo.setLastName(user.getLastName());
                userInfo.setEmail(user.getEmail());

                if (user.getAddress() != null) {
                    ru.zettatech.userservice.response.dto.Address address =
                            new ru.zettatech.userservice.response.dto.Address();
                    address.setAddressId(user.getAddress().getAddressId());
                    address.setAddressLine(user.getAddress().getAddressLine());
                    address.setCity(user.getAddress().getCity());
                    address.setPin(user.getAddress().getPin());
                    address.setState(user.getAddress().getState());
                    address.setCountry(user.getAddress().getCountry());
                    userInfo.setAddress(address);
                }

                userInfo.setCartId(user.getCartId());
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                response.setCode("404");
                response.setMessage("User information not found");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error("Error getting user info", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request) {
        ServiceResponse response = new ServiceResponse();

        try {
            User user = userRepository.findByUserId(request.getUserId()).orElse(null);
            if (user != null) {
                Address address = new Address();
                address.setAddressLine(request.getAddressLine());
                address.setCity(request.getCity());
                address.setPin(request.getPin());
                address.setState(request.getState());
                address.setCountry(request.getCountry());
                user.setAddress(address);
                userRepository.save(user);
                response.setCode("200");
                response.setMessage("Address information Synced");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.setCode("404");
            response.setMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating address", e);
            response.setCode("500");
            response.setMessage("Internal Server Error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
