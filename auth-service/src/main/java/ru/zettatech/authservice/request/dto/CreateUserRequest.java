package ru.zettatech.authservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
}
