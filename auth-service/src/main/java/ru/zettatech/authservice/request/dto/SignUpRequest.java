package ru.zettatech.authservice.request.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {

	private String username;
	@Email(message = "email is required")
	private String email;
	@NotBlank(message = "first name is required")
	private String firstName;
	@NotBlank(message = "last name is required")
	private String lastName;
	@NotBlank(message = "password is required")
	private String password;
	@NotBlank(message = "re-enter Password")
	private String confirmPassword;
	
}
