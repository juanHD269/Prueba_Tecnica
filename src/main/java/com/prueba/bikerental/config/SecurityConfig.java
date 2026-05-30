package com.prueba.bikerental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll()
				)
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.httpBasic(Customizer.withDefaults())
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService(
			@Value("${app.security.username}") String username,
			@Value("${app.security.password}") String rawPassword,
			PasswordEncoder passwordEncoder
	) {
		UserDetails user = User.builder()
				.username(username)
				.password(passwordEncoder.encode(rawPassword))
				.roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
