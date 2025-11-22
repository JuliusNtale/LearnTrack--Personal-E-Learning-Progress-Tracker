package com.hextech.learntrack.service;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User registerUser(User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("STUDENT"); // Default role
        user.setEnabled(false); // Disable until email verification
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public boolean verifyEmail(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if token is still valid (24 hours)
            if (user.getTokenCreatedAt().plusHours(24).isAfter(LocalDateTime.now())) {
                user.setEnabled(true);
                user.setVerificationToken(null);
                user.setTokenCreatedAt(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public String generatePasswordResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setTokenCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            return token;
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword, PasswordEncoder passwordEncoder) {
        Optional<User> userOpt = userRepository.findByPasswordResetToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if token is still valid (1 hour)
            if (user.getTokenCreatedAt().plusHours(1).isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setPasswordResetToken(null);
                user.setTokenCreatedAt(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countByRole(String role) {
        return userRepository.countByRole(role);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}