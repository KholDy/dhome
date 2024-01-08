package com.github.kholdy.dhome.config;

import com.github.kholdy.dhome.model.User;
import com.github.kholdy.dhome.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailsService(UserRepository userRepo) {
		return username -> {
			User user = userRepo.findByUsername(username);
			if(user != null) return user;

			throw new UsernameNotFoundException("User '" + username + "' not found");
		};
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers(
							new AntPathRequestMatcher("/home"),
							new AntPathRequestMatcher("/home/**"),
							new AntPathRequestMatcher("/api/**")).hasRole("USER");
					auth.requestMatchers(
							new AntPathRequestMatcher("/"),
							new AntPathRequestMatcher("/**"),
							new AntPathRequestMatcher("/register"),
							new AntPathRequestMatcher("/register/**")).permitAll();
					auth.anyRequest().authenticated();
				})
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
