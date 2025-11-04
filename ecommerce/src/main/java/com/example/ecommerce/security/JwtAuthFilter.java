package com.example.ecommerce.security;

import com.example.ecommerce.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    /*
     * This method intercepts every HTTP request to handle JWT-based authentication.
     * It checks for a JWT token in cookies, validates it, and sets authentication
     * in the Spring Security context if valid.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String token = null;

        // 1️⃣ Extract JWT from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        // 2️⃣ If no token, continue filter chain (unauthenticated)
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 3️⃣ Validate token and set authentication if valid
            if (jwtService.isTokenValid(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                String email = jwtService.extractEmail(token);

                // Assign default role (you can extend this later with real roles)
                List<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            // 4️⃣ Token invalid or expired — clear context and optionally set response
            SecurityContextHolder.clearContext();
            // Optionally, you can set 401 response if needed for protected APIs:
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // return; // Uncomment if you want to stop filter chain on invalid token
        }

        // Continue processing request
        chain.doFilter(request, response);
    }
}

