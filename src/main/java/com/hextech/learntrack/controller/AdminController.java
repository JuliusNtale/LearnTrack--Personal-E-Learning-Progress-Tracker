package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public String manageUsers(@AuthenticationPrincipal User currentUser,
                              Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{userId}/toggle-status")
    public String toggleUserStatus(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElseThrow();
        user.setEnabled(!user.isEnabled());
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/change-role")
    public String changeUserRole(@PathVariable Long userId,
                                 @RequestParam String role) {
        User user = userService.getUserById(userId).orElseThrow();
        user.setRole(role);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
}