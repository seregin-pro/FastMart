package ru.zettatech.cartservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Arrays;

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//
//    @Autowired
//    private Environment environment;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())                     // отключаем CSRF-защиту
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // управление сессиями
//                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())  // аутентификация всех запросов
//                .httpBasic(Customizer.withDefaults());                   // базовая HTTP-аутентификация
//
//        return http.build();                                   // строим цепочку фильтров
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManager.class);
//    }
//
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        String username = environment.getProperty("security.user.name");
//        String password = environment.getProperty("security.user.password");
//
//        return new InMemoryUserDetailsManager(Arrays.asList(
//                User.builder()
//                        .username(username)
//                        .password(encoder.encode(password))
//                        .authorities("ROLE_USER")      // роли пользователя
//                        .build()));
//    }
}
