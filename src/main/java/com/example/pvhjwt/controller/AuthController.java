package com.example.pvhjwt.controller;

import com.example.pvhjwt.jwt.JwtServiceImp;
import com.example.pvhjwt.model.UserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImp jwtServiceImp;

    public AuthController(AuthenticationManager authenticationManager, JwtServiceImp jwtServiceImp) {
        this.authenticationManager = authenticationManager;
        this.jwtServiceImp = jwtServiceImp;
    }

    @PostMapping
    public String login(@RequestBody UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (userRequest.getEmail(), userRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtServiceImp.generateToken(userRequest.getEmail());
        }

        return null;
    }
}
