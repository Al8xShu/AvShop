package com.avs.shop.controllers;

import com.avs.shop.domain.User;
import com.avs.shop.dto.UserDTO;
import com.avs.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO dto, Model model) {
        if (userService.save(dto)) {
            User user = userService.findByName(dto.getUsername());
            return "redirect:/users";
        } else {
            model.addAttribute("user", dto);
            return "user";
        }
    }

    @RequestMapping("/delete")
    public String deleteUser(String username) {
        if (username != null && !username.equals("admin")) {
            User user = userService.findByName(username);
            userService.delete(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize!");
        }
        User user = userService.findByName(principal.getName());
        UserDTO dto = UserDTO.builder().username(user.getName()).email(user.getEmail()).build();
        model.addAttribute("user", dto);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())) {
            throw new RuntimeException("You are not authorize!");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("user", dto);
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode) {
        boolean activate = userService.activateUser(activateCode);
        model.addAttribute("activated", activate);
        return "activate-user";
    }

}
