package com.github.kholdy.dhome.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email")
		})
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Username cannot be empty.")
	private String username;
	
	@NotEmpty(message = "Password cannot be empty.")
	@Size(min = 8, max = 100, message = "Password most be min = 8 and max = 100 symbols.")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@NotEmpty(message = "Email cannot be empty.")
	@Email(message = "Email is not valid")
	private String email;

	private String fullname;

	private String country;

	private String city;

	private String phoneNumber;

	public User(String username, String password, String email, String fullname, String country, String city, String phoneNumber) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.country = country;
		this.city = city;
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		String role = "";
		for (Role r: roles) {
			role += r.getName() + " ";
		}
		return role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
