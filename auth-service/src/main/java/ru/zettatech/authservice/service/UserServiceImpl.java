package ru.zettatech.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zettatech.authservice.entity.Role;
import ru.zettatech.authservice.entity.User;
import ru.zettatech.authservice.model.UserPrincipal;
import ru.zettatech.authservice.repository.RoleRepository;
import ru.zettatech.authservice.repository.UserRepository;
import ru.zettatech.authservice.request.dto.SignUpRequest;
import ru.zettatech.authservice.response.dto.ServiceResponse;
import ru.zettatech.authservice.response.dto.SignUpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            return UserPrincipal.build(user);
        }
        throw new UsernameNotFoundException("User not found with username -> : " + username);
    }

    @Transactional
    public User loadUserById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> signUp(SignUpRequest request) {
        ServiceResponse serviceResponse = new ServiceResponse();
        SignUpResponse signUpResponse = new SignUpResponse();

        try {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                serviceResponse.setCode("400");
                serviceResponse.setMessage("Please enter the same password as in the password field");
                return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
            }

            if (userRepository.findUserByUsername(request.getUsername()) != null) {
                serviceResponse.setCode("400");
                serviceResponse.setMessage("User already registered with " + request.getUsername() + " please try with a different username");
                return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
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
                signUpResponse.setMessage(request.getUsername() + " you are successfully registered");
                return new ResponseEntity<>(signUpResponse, HttpStatus.OK);
            }

            serviceResponse.setCode("401");
            serviceResponse.setMessage(request.getUsername() + " we are not accepting user registration at the moment. Please try again later");
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("There is an Error while SignUp: {}", e.toString());
            serviceResponse.setCode("501");
            serviceResponse.setMessage("There is an error occurred while signing you up. Please try after some time");
            return new ResponseEntity<>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
