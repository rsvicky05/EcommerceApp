package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private JwtService jwtService;

    /**
     * Register a new user.
     */
    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists!";
        }

        // Generate salt and hash password
        String salt = passwordService.generateSalt();
        String hashedPassword = passwordService.hashPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return "User registered successfully!";
    }

    /**
     * Sign in an existing user.
     */
    public String loginUser(String email, String password) {
        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        logger.info(password);
        if (existingUserOpt.isEmpty()) {
            return "Invalid credentials!";
        }

        User existingUser = existingUserOpt.get();

        // Verify password
        boolean isValid = passwordService.verifyPassword(password, existingUser.getSalt(), existingUser.getPassword());
        if (!isValid) {
            return "Invalid credentials!";
        }

        // Generate JWT token
        String token = jwtService.generateToken(existingUser.getEmail());
        return token;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
