package ru.zettatech.authservice.security;

import org.springframework.security.core.GrantedAuthority;
import ru.zettatech.authservice.entity.Role;
import ru.zettatech.authservice.entity.User;
import ru.zettatech.authservice.model.UserPrincipal;
import ru.zettatech.authservice.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JWTTokenProvider {

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration_in_ms}")
	private long expirationTime;

	@Value("${app.jwt.header_string}")
	private String headerString;

	@Autowired
	private UserRepository userRepository;

	// Generate the token
	public String generateToken(Authentication authentication){
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Date date = new Date(System.currentTimeMillis());
		Date expiryDate = new Date(date.getTime() + expirationTime);
		User user = userRepository.findById(userPrincipal.getId()).orElse(null);
		Set<Role> roles = null;
		if (!(user == null)) {
			roles = user.getRoles();
		}
		List<String> authList = new ArrayList<>();
        assert roles != null;
        for (Role role : roles) {
			authList.add(role.getName());
		}

		Long userId = userPrincipal.getId();

		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("userName", userPrincipal.getUsername());
		claims.put("firstName", userPrincipal.getFirstName());
		claims.put("lastName", userPrincipal.getLastName());
		claims.put("email", userPrincipal.getEmail());
		claims.put("authList", authList);

		return Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

	}

	// validate token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
			return true;
		} catch (io.jsonwebtoken.SignatureException ex) {
			log.error("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token" + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty");
		}

		return false;
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseSignedClaims(token).getPayload();

		String username = (String) claims.get("userName");
		return username;
	}
}
