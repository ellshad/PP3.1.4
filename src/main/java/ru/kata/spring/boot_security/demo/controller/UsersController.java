package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Controller
@RequestMapping("/")
public class UsersController {
    private final UserServiceImpl userService;


    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("admin")
    public String listUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("listRoles", userService.listRoles());
        model.addAttribute("listUser", userService.getAllUsers());
        model.addAttribute("user", user);
        return "adminPage";
    }

    @GetMapping("user")
    public String infoUser(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "userPage";
    }

    @PostMapping(value = "create")
    public String newUser(@ModelAttribute User user) {
        if (userService.isExistEmail(user)){
            return "emailExists";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @PostMapping("/update/{id}")
    public String update(@ModelAttribute User user) {
        if (userService.isExistEmail(user)){
            return "emailExists";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}