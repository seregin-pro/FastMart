package ru.zettatech.authservice.request.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInRequest {

	@NotBlank(message = "username cannot be blank")
	private String username;
	@NotBlank(message = "password cannot be blank")
	private String password;
	
}
