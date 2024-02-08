package com.github.kholdy.dhome.config;

import com.github.kholdy.dhome.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Order(1)
	SecurityFilterChain restApiFilterChain(HttpSecurity http) throws Exception {
		return  http
				.csrf().disable()
				.securityMatcher("/api/**")
				.authorizeHttpRequests(auth -> {
					auth.anyRequest().authenticated();
				})
				//.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.userDetailsService(userService)
				.httpBasic(Customizer.withDefaults())
				.build();
	}
	
	@Bean
	@Order(2)
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers(
							AntPathRequestMatcher.antMatcher("/home"),
							AntPathRequestMatcher.antMatcher("/home/**")).hasRole("USER");
					auth.requestMatchers(
							AntPathRequestMatcher.antMatcher("/admin"),
							AntPathRequestMatcher.antMatcher("/admin/**")).hasRole("ADMIN");
					auth.requestMatchers(
							AntPathRequestMatcher.antMatcher("/"),
							AntPathRequestMatcher.antMatcher("/**"),
							AntPathRequestMatcher.antMatcher("/register"),
							AntPathRequestMatcher.antMatcher("/register/**")).permitAll();
					auth.anyRequest().authenticated();
				})
				.userDetailsService(userService)
				.formLogin(form -> {
					form.loginPage("/")
					.defaultSuccessUrl("/home", true)
					.permitAll();
				})
				.logout(x -> {
					x.logoutSuccessUrl("/");
				})
				.httpBasic(Customizer.withDefaults())
				.build();
	}
}
