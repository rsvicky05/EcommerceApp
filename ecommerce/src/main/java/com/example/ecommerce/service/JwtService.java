package com.example.ecommerce.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Token expiry: 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /** 🔐 Get signing key from secret */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** 🪙 Generate JWT token using user's email */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) // uses HS256 automatically
                .compact();
    }

    /** ✅ Validate token (checks signature + expiry) */
    public boolean isTokenValid(String token) {
        try {
            // 🔒 This line parses and verifies the signature before proceeding
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // invalid signature, expired, or malformed
        }
    }

    /** 🧩 Extract email (subject) from token */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** 🔍 Generic claim extractor */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** ⏳ Check if token expired */
    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    /** Get token expiration date */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** 🧾 Parse and extract all claims (throws if invalid/expired) */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)  // verifies signature automatically
                .getBody();
    }
}
