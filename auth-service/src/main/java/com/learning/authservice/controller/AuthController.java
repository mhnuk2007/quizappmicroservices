package com.learning.authservice.controller;

import com.learning.authservice.dto.SignInRequest;
import com.learning.authservice.dto.SignInResponse;
import com.learning.authservice.dto.SignupRequest;
import com.learning.authservice.security.JwtService;
import com.learning.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody SignupRequest request) {
        return new ResponseEntity<>(authService.signUp(request), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authService.SingIn(request), HttpStatus.OK);
    }

    // New endpoint for API Gateway to validate token
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        try {
            String username = jwtService.extractUsername(token);
            boolean valid = !jwtService.isTokenExpired(token);

            if (valid) {
                return ResponseEntity.ok("Token is valid for user: " + username);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
