// package com.example.demo.controller.auth;

// import com.example.demo.entity.user.User;
// import com.example.demo.repository.user.UserRepository;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Optional;
// import java.util.concurrent.TimeUnit;

// @RestController
// @RequestMapping("/api/auth")
// // @CrossOrigin(origins = "*", allowCredentials = "true")
// @CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
// public class AuthController {

//     @Autowired
//     private UserRepository userRepository;

//     // Generate JWT token and save user if not exists
//     @PostMapping("/login")
//     public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
//         try {
//             String jwtToken = generateJWT(request.getUsername(), request.getPassword());
//             Long userId = createOrUpdateUser(request.getUsername());

//             Map<String, Object> response = new HashMap<>();
//             response.put("token", jwtToken);
//             response.put("userId", userId);
//             response.put("username", request.getUsername());
//             response.put("success", true);

//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("success", false);
//             errorResponse.put("message", "Invalid username or password");
//             return ResponseEntity.status(401).body(errorResponse);
//         }
//     }

//     @PostMapping("/register")
//     public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
//         try {
//             String jwtToken = generateJWT(request.getUsername(), request.getPassword());
//             Long userId = createOrUpdateUser(request.getUsername());

//             Map<String, Object> response = new HashMap<>();
//             response.put("token", jwtToken);
//             response.put("userId", userId);
//             response.put("username", request.getUsername());
//             response.put("success", true);
//             response.put("message", "User created successfully");

//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("success", false);
//             errorResponse.put("message", "Username already exists");
//             return ResponseEntity.status(409).body(errorResponse);
//         }
//     }

//     private Long createOrUpdateUser(String username) {
//         Optional<User> userOptional = userRepository.findByUsername(username);
//         if (userOptional.isPresent()) {
//             return userOptional.get().getId();
//         } else {
//             User user = new User();
//             user.setUsername(username);
//             user.setPassword(generatePasswordHash(username + "password123")); // In production, use secure password input
//             userRepository.save(user);
//             return user.getId();
//         }
//     }

//     private String generateJWT(String username, String password) {
//         long expirationTime = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        
//         Map<String, Object> claims = new HashMap<>();
//         claims.put("username", username);
        
//         String token = generateToken(claims, expirationTime);
        
//         return token;
//     }

//     private String generateToken(Map<String, Object> claims, long expirationTime) {
//         // Using a simple JWT generation approach
//         // In production, use jjwt or spring-security-oauth-jwt library
//         return "Bearer " + createJwtToken(claims, expirationTime);
//     }

//     private String createJwtToken(java.util.Map<String, Object> claims, long ttl) {
//         // Simplified JWT - replace with proper JWT library implementation
//         // This is a demo token that encodes the username
//         try {
//             java.util.Date now = new java.util.Date();
//             java.util.Date expiry = new java.util.Date(now.getTime() + ttl);
            
//             String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
//             String payload = "{ \"username\": \"" + claims.get("username") + "\" ,\"exp\":" + 
//                 Math.floor(expiry.getTime() / 1000) + " }";
            
//             String headerEncoded = java.util.Base64.getEncoder().encodeToString((header + ".").getBytes());
//             String payloadEncoded = java.util.Base64.getEncoder().encodeToString(payload.getBytes());
            
//             return headerEncoded + "." + payloadEncoded;
//         } catch (Exception e) {
//             throw new RuntimeException("Error generating JWT token", e);
//         }
//     }

//     private String generatePasswordHash(String password) {
//         // In production, use BCrypt or Argon2
//         return java.security.MessageDigest.isEqual(password.getBytes(), 
//             "password123".getBytes()) ? "hash" : password;
//     }

//     public static class LoginRequest {
//         private String username;
//         private String password;

//         public String getUsername() {
//             return username;
//         }

//         public void setUsername(String username) {
//             this.username = username;
//         }

//         public String getPassword() {
//             return password;
//         }

//         public void setPassword(String password) {
//             this.password = password;
//         }
//     }

//     public static class RegisterRequest {
//         private String username;
//         private String password;

//         public String getUsername() {
//             return username;
//         }

//         public void setUsername(String username) {
//             this.username = username;
//         }

//         public String getPassword() {
//             return password;
//         }

//         public void setPassword(String password) {
//             this.password = password;
//         }
//     }
// }
package com.example.demo.controller.auth;

import com.example.demo.entity.user.User;
import com.example.demo.repository.user.UserRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
    origins = {
        "http://localhost:3000",
        "http://localhost:5173"
    },
    allowCredentials = "true"
)
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByUsername(request.getUsername());

        // User doesn't exist
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(
                errorResponse("Invalid username or password")
            );
        }

        User user = userOptional.get();

        // Password doesn't match
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return ResponseEntity.status(401).body(
                errorResponse("Invalid username or password")
            );
        }

        // Credentials are correct
        String jwtToken =
                generateJWT(user.getUsername());

        Map<String, Object> response = new HashMap<>();

        response.put("token", jwtToken);
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }


    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        Optional<User> existingUser =
                userRepository.findByUsername(request.getUsername());

        // Username already exists
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body(
                errorResponse("Username already exists")
            );
        }

        // Create new user
        User user = new User();

        user.setUsername(request.getUsername());

        // HASH THE PASSWORD
        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        String jwtToken =
                generateJWT(user.getUsername());

        Map<String, Object> response = new HashMap<>();

        response.put("token", jwtToken);
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("success", true);
        response.put("message", "User created successfully");

        return ResponseEntity.ok(response);
    }


    // =========================
    // JWT
    // =========================

    private String generateJWT(String username) {

        long expirationTime =
                24 * 60 * 60 * 1000;

        Map<String, Object> claims =
                new HashMap<>();

        claims.put("username", username);

        return generateToken(
                claims,
                expirationTime
        );
    }


    private String generateToken(
            Map<String, Object> claims,
            long expirationTime) {

        return "Bearer " +
                createJwtToken(
                    claims,
                    expirationTime
                );
    }


    private String createJwtToken(
            Map<String, Object> claims,
            long ttl) {

        try {

            java.util.Date now =
                    new java.util.Date();

            java.util.Date expiry =
                    new java.util.Date(
                        now.getTime() + ttl
                    );

            String header =
                    "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

            String payload =
                    "{ \"username\": \"" +
                    claims.get("username") +
                    "\" ,\"exp\":" +
                    Math.floor(
                        expiry.getTime() / 1000
                    ) +
                    " }";

            String headerEncoded =
                    java.util.Base64
                        .getEncoder()
                        .encodeToString(
                            header.getBytes()
                        );

            String payloadEncoded =
                    java.util.Base64
                        .getEncoder()
                        .encodeToString(
                            payload.getBytes()
                        );

            return headerEncoded +
                    "." +
                    payloadEncoded;

        } catch (Exception e) {

            throw new RuntimeException(
                "Error generating JWT token",
                e
            );
        }
    }


    private Map<String, Object> errorResponse(
            String message) {

        Map<String, Object> response =
                new HashMap<>();

        response.put("success", false);
        response.put("message", message);

        return response;
    }


    // =========================
    // REQUEST DTOs
    // =========================

    public static class LoginRequest {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    public static class RegisterRequest {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}