package com.github.kholdy.dhome.web;

import com.github.kholdy.dhome.data.RoleRepository;
import com.github.kholdy.dhome.data.UserRepository;
import com.github.kholdy.dhome.model.Role;
import com.github.kholdy.dhome.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute(name = "listOfUsers")
    public Iterable<User> addListOfUsers() {
        return userRepository.findAll();
    }

    @GetMapping
    public String getAdmin(Model model) {
        getLoggedUser(model);

        return "admin";
    }

    @GetMapping("/user-edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User userChange = userRepository.findById(id).get();
        Iterable<Role> listRoles = roleRepository.findAll();
        model.addAttribute("user", userChange);
        model.addAttribute("listRoles", listRoles);

        getLoggedUser(model);
        return "user-edit";
    }

    @PostMapping("/user-edit/save")
    public String saveUser(@ModelAttribute("user") User newUser) {

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return "admin";
    }

    private void getLoggedUser(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        String username = loggedInUser.getName();
        User user = userRepository.findByUsername(username).get();
        model.addAttribute("username", user.getUsername());

        for (Role  r:  user.getRoles()) {
            if (r.getName().equals("ROLE_ADMIN")) {
                model.addAttribute("role", r.getName());
            }
        }
    }
}
