package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordService {

    @Value("${app.security.pepper}")
    private String pepper;

    public String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashPassword(String password, String salt) {
        String combined = password + salt + pepper;
        return BCrypt.hashpw(combined, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String salt, String hashedPassword) {
        String combined = password + salt + pepper;
        return BCrypt.checkpw(combined, hashedPassword);
    }
}
