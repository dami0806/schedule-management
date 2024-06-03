package com.sparta.scheduleapp.auth.service;

import com.sparta.scheduleapp.auth.entity.LoginRequest;
import com.sparta.scheduleapp.auth.entity.SignupRequest;
import com.sparta.scheduleapp.auth.entity.User;
import com.sparta.scheduleapp.auth.entity.UserRoleEnum;
import com.sparta.scheduleapp.auth.repository.UserRepository;
import com.sparta.scheduleapp.auth.util.JwtUtil;
import com.sparta.scheduleapp.exception.InfoNotCorrectedException;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.UnauthorizedException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    void signup(SignupRequest signupRequest);
    String login(LoginRequest loginRequest);

}