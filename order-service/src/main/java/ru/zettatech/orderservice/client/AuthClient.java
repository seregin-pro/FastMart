package ru.zettatech.orderservice.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${auth-service.url:http://localhost:8081}")
    private String authServiceUrl;

    public boolean userExists(String username) {
        try {
            String url = authServiceUrl + "/api/users/me";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Name", username);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            log.warn("Auth service unavailable for user {}: {}", username, e.getMessage());
            return false;
        }
    }

    public UserInfo getUserInfo(String username) {
        try {
            String url = authServiceUrl + "/api/users/me";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Name", username);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> user = response.getBody();
                UserInfo info = new UserInfo();
                info.setUsername((String) user.get("username"));
                info.setFirstName((String) user.get("firstName"));
                info.setLastName((String) user.get("lastName"));
                info.setEmail((String) user.get("email"));
                return info;
            }
            return null;
        } catch (RestClientException e) {
            log.warn("Auth service unavailable for user {}: {}", username, e.getMessage());
            return null;
        }
    }

    @Getter
    @Setter
    public static class UserInfo {
        private String username;
        private String firstName;
        private String lastName;
        private String email;
    }
}
