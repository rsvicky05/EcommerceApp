    package com.example.ecommerce.controller;

    import com.example.ecommerce.model.User;
    import com.example.ecommerce.service.JwtService;
    import com.example.ecommerce.service.UserService;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletResponse;
    //import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.ResponseCookie;


    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;
    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public class AuthController {

        @Autowired
        private UserService userService;

        @GetMapping("/validate")
        public ResponseEntity<?> validateToken() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity.ok("Token valid");
            } else {
                return ResponseEntity.status(401).body("Invalid or missing token");
            }
        }

        /**
         * 📝 Register a new user (Sign Up)
         */
        @PostMapping("/signup")
        public ResponseEntity<String> registerUser(@RequestBody User user) {
            String response = userService.registerUser(user);

            if (response.contains("exists")) {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok("User registered successfully");
        }

        /**
         * 🔐 Authenticate user and set JWT in cookie (Sign In)
         */




        @PostMapping("/signin")
        public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletResponse response) {
            String token = userService.loginUser(user.getEmail(), user.getPassword());
            if (token.startsWith("Invalid")) {
                return ResponseEntity.badRequest().body(token);
            }

            // ✅ Encode token safely
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

            // ✅ Use ResponseCookie
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", encodedToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(24 * 60 * 60)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            return ResponseEntity.ok("Login successful");
        }



        /**
         * 🚪 Log out user by clearing JWT cookie
         */
        @PostMapping("/logout")
        public ResponseEntity<String> logoutUser(HttpServletResponse response) {
            // Use ResponseCookie to match login cookie attributes exactly
            ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                    .httpOnly(true)
                    .secure(false)       // must match login
                    .path("/")           // must match login
                    .sameSite("Lax")     // must match login
                    .maxAge(0)           // expires immediately
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
            return ResponseEntity.ok("Logged out successfully");
        }

    }
