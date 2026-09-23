package com.example.demo.controller;

import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService service;

    
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    AuthController(AuthenticationManager authManager,JwtUtil jwtUtil,UserService service) {
        this.jwtUtil = jwtUtil;
        this.authManager= authManager;
        this.service=service;

    }
    
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        service.register(user);
        return "User registered";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody User user) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()));
        } catch (Exception e) {
            return "Login Failed ❌"; // debug
        }

        return jwtUtil.generateToken(user.getUsername());
    }

    @GetMapping("/user")
    public String user() {
        return "Hello User";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }
}