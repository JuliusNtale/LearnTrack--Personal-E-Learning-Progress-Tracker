package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.UserService;
import com.hextech.learntrack.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model) {

        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // Register user and send verification email
        User registeredUser = userService.registerUser(user, passwordEncoder);
        emailService.sendVerificationEmail(user.getEmail(), registeredUser.getVerificationToken());
        
        return "redirect:/login?registered";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        boolean isVerified = userService.verifyEmail(token);
        if (isVerified) {
            model.addAttribute("message", "Email verified successfully! You can now log in.");
            return "auth/login";
        } else {
            model.addAttribute("error", "Invalid or expired verification token.");
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        String token = userService.generatePasswordResetToken(email);
        if (token != null) {
            emailService.sendPasswordResetEmail(email, token);
        }
        model.addAttribute("message", "If an account exists with this email, a password reset link has been sent.");
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        // Token validation will be done in POST method
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String password,
                                       Model model) {
        boolean isReset = userService.resetPassword(token, password, passwordEncoder);
        if (isReset) {
            model.addAttribute("message", "Your password has been reset successfully. You can now log in with your new password.");
            return "auth/login";
        } else {
            model.addAttribute("error", "Invalid or expired reset token.");
            return "auth/reset-password";
        }
    }
}
