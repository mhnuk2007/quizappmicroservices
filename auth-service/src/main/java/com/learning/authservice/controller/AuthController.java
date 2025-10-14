package com.learning.authservice.controller;

import com.learning.authservice.dto.SignInRequest;
import com.learning.authservice.dto.SignInResponse;
import com.learning.authservice.dto.SignupRequest;
import com.learning.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody SignupRequest request) {
        return new ResponseEntity<>(authService.signUp(request), HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authService.SingIn(request), HttpStatus.OK);
    }


}
