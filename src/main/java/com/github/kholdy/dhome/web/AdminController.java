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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute(name = "listOfUsers")
    public Iterable<User> addListOfUsers() {
        return userRepository.findAll();
    }

    @GetMapping
    public String getAdmin(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        String username = loggedInUser.getName();
        User user = userRepository.findByUsername(username).get();
        model.addAttribute("username", user.getUsername());

        return "admin";
    }

    @GetMapping("/user-edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).get();
        Iterable<Role> listRoles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "user-edit";
    }

    @PostMapping("/user-edit/save")
    public String saveUser(@ModelAttribute("user") User newUser) {

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return "admin";
    }
}
