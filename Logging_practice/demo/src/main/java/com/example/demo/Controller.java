package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/user")
    public String getUser() {

        return "User Found";
    }

    @GetMapping("/warn")
    public String warning() {

        return null;
    }

    @GetMapping("/error")
    public String error() {

        throw new RuntimeException("Something went wrong");
    }
}