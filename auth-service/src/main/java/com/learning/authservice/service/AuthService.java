package com.learning.authservice.service;

import com.learning.authservice.Repository.UserRepository;
import com.learning.authservice.dto.SignInRequest;
import com.learning.authservice.dto.SignInResponse;
import com.learning.authservice.dto.SignupRequest;
import com.learning.authservice.model.Role;
import com.learning.authservice.model.User;
import com.learning.authservice.security.CustomUserDetails;
import com.learning.authservice.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signUp(SignupRequest request) {
        if (userRepository.existsUserByUsername(request.getUsername())) {
            return "Username already exists";
    }
        if (userRepository.existsUserByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return "User registered successfully";

    }

    public SignInResponse SingIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new RuntimeException("User not found"));
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new SignInResponse(token, "User logged in successfully");

    }
}
