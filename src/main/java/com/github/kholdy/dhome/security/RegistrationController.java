package com.github.kholdy.dhome.security;

import java.util.List;

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
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(@ModelAttribute("user") @Valid User user, BindingResult result) {
		String err = "A user with the same username or email already exists";

		// Поиск пользователей у которых username и email совподают с введенными в форме регистрации
		List<User> listOfUsers = (List<User>) userRepo.findAll();
		List<User> usersWithSameUsername =
									listOfUsers.
									   stream().
									   filter((User u) -> u.getUsername().equals(user.getUsername())).toList();

		List<User> usersWithSameEmail =
									listOfUsers.
									   stream().
									   filter((User u) -> u.getEmail().equals(user.getEmail())).toList();

		if (result.hasErrors()) {
			return "registration";
		}

		// Если пользователь с таким username и email уже существуют бд,
		// то выдаем ошибку и просим ввести другие username и email
		if(usersWithSameUsername.isEmpty() && usersWithSameEmail.isEmpty()) {
			userRepo.save(new User(user.getUsername(),
								   passwordEncoder.encode(user.getPassword()),
								   user.getEmail(),
								   user.getFullname(),
								   user.getCountry(),
								   user.getCity(),
								   user.getPhoneNumber()));
		} else {
			ObjectError error = new ObjectError("globalError", err);
	        result.addError(error);
	        return "registration";
		}

		return "redirect:/";
	}
	
}
