package com.sparta.scheduleapp.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html
    }
}
