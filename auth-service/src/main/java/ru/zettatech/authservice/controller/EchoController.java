package ru.zettatech.authservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class EchoController {

	@GetMapping("/")
	@PreAuthorize("hasRole('USER')")
	public String echo(Principal principal) {
		return principal.getName();
	}

}
