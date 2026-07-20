package ru.zettatech.authservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.zettatech.authservice.service.UserServiceImpl;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
		prePostEnabled = true,
		jsr250Enabled = true,
		securedEnabled = true
)
public class SecurityConfig {

	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final UserServiceImpl userDetailsService;

	public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler, UserServiceImpl userDetailsService) {
		this.unauthorizedHandler = unauthorizedHandler;
		this.userDetailsService = userDetailsService;
	}

	// Бин для кастомного фильтра JWT
	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	}

	// Основная конфигурация цепочки фильтров безопасности (SecurityFilterChain)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Настройка CORS (если требуется)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				// Отключаем CSRF, так как мы не храним сессию на клиенте (Stateless)
				.csrf(AbstractHttpConfigurer::disable)
				.exceptionHandling(exception ->
						exception.authenticationEntryPoint(unauthorizedHandler))
				// Аутентификация по токенам, без сессий
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Для H2 Console
				.headers(headers ->
						headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

		// Разрешаем всем доступ к эндпоинтам аутентификации (/api/auth/**)
		// И ЗАЩИЩАЕМ админ-эндпоинты (/api/admin/**), требуя аутентификации.
		http.authorizeHttpRequests(auth -> auth
				// РАЗРЕШИТЬ ДЛЯ ВСЕХ (Логин, Регистрация)
				.requestMatchers("/api/auth/**").permitAll()
				// ТРЕБОВАТЬ АУТЕНТИФИКАЦИЮ (БЫЛО permitAll())
				.requestMatchers("/api/admin/**").authenticated()
				// Все остальные запросы тоже требуют аутентификации
				.anyRequest().authenticated()
		);

		// Добавляем наш JWT фильтр перед стандартным фильтром Username/Password
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Бин AuthenticationManager, используется для процесса аутентификации (логина)
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}

	// Бин для кодирования паролей с помощью BCrypt
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Конфигурация CORS для фронтенд-приложений (например, на localhost:3000)
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// В продакшене НЕ используйте "*" для allowedOrigins из соображений безопасности.
		// Замените на конкретный домен вашего фронтенда.
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowCredentials(true); // Важно для передачи кук/авторизации
		configuration.setAllowedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
