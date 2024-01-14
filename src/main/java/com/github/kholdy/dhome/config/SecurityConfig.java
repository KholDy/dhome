package com.github.kholdy.dhome.config;

import com.github.kholdy.dhome.model.User;
import com.github.kholdy.dhome.data.UserRepository;
import com.github.kholdy.dhome.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Order(1)
	SecurityFilterChain restApiFilterChain(HttpSecurity http) throws Exception {
		return  http
				.securityMatcher("/api/**")
				.authorizeHttpRequests(auth -> {
					auth.anyRequest().authenticated();
				})
				//.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.userDetailsService(jpaUserDetailsService)
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
							AntPathRequestMatcher.antMatcher("/"),
							AntPathRequestMatcher.antMatcher("/**"),
							AntPathRequestMatcher.antMatcher("/register"),
							AntPathRequestMatcher.antMatcher("/register/**")).permitAll();
					auth.anyRequest().authenticated();
				})
				.userDetailsService(jpaUserDetailsService)
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
