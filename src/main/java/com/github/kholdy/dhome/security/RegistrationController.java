package com.github.kholdy.dhome.security;

import java.util.Collections;
import java.util.List;

import com.github.kholdy.dhome.data.RoleRepository;
import com.github.kholdy.dhome.model.Role;
import com.github.kholdy.dhome.model.User;
import com.github.kholdy.dhome.data.UserRepository;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;

	public RegistrationController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(@ModelAttribute("user") @Valid User newUser, BindingResult result) {
		String err = "A user with the same username or email already exists";

		// Поиск пользователей у которых username и email совподают с введенными в форме регистрации
		List<User> listOfUsers = (List<User>) userRepo.findAll();
		List<User> usersWithSameUsername =
									listOfUsers.
									   stream().
									   filter((User u) -> u.getUsername().equals(newUser.getUsername())).toList();

		List<User> usersWithSameEmail =
									listOfUsers.
									   stream().
									   filter((User u) -> u.getEmail().equals(newUser.getEmail())).toList();

		if (result.hasErrors()) {
			return "registration";
		}

		// Если пользователь с таким username и email уже существуют бд,
		// то выдаем ошибку и просим ввести другие username и email
		if(usersWithSameUsername.isEmpty() && usersWithSameEmail.isEmpty()) {
			Role role = roleRepo.findByName("ROLE_USER").get();
			User user = new User(newUser.getUsername(),
								passwordEncoder.encode(newUser.getPassword()),
								newUser.getEmail(),
								newUser.getFullname(),
								newUser.getCountry(),
								newUser.getCity(),
								newUser.getPhoneNumber());
			user.getRoles().add(role);

			userRepo.save(user);
		} else {
			ObjectError error = new ObjectError("globalError", err);
	        result.addError(error);
	        return "registration";
		}

	return "redirect:/";
	}
	
}
