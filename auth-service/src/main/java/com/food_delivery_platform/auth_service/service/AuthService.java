package com.food_delivery_platform.auth_service.service;

import com.food_delivery_platform.auth_service.dto.*;
import com.food_delivery_platform.auth_service.entity.User;
import com.food_delivery_platform.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserResponse signup(UserDTO userDTO) {
        // Check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());

        // Save user
        User savedUser = userRepository.save(user);

        // Return user response
        return new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getRole()
        );
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by username or email
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsernameOrEmail());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(loginRequest.getUsernameOrEmail());
        }

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid username/email or password!");
        }

        User user = userOptional.get();

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username/email or password!");
        }

        // Generate JWT token
        String token = jwtService.generateToken(
            user.getUsername(),
            user.getId(),
            user.getRole().toString()
        );

        // Return login response
        return new LoginResponse(
            token,
            "Bearer",
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }

    public UserResponse getUserProfile(String token) {
        // Extract token without "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Validate token
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token!");
        }

        // Get username from token
        String username = jwtService.getUsernameFromToken(token);
        if (username == null) {
            throw new RuntimeException("Invalid token!");
        }

        // Find user
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = userOptional.get();

        // Return user profile
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }

    public boolean validateToken(String token) {
        // Extract token without "Bearer " prefix
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return jwtService.validateToken(token);
    }
}
