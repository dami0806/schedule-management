package com.sparta.scheduleapp.auth.controller;

import com.sparta.scheduleapp.auth.dto.LoginRequestDto;
import com.sparta.scheduleapp.auth.dto.SignupRequestDto;
import com.sparta.scheduleapp.auth.dto.TokenResponseDto;
import com.sparta.scheduleapp.auth.service.UserService;
import com.sparta.scheduleapp.auth.util.JwtUtil;
import com.sparta.scheduleapp.comment.dto.RefreshTokenRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthRestController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        String refreshToken = jwtUtil.createRefreshToken(loginRequestDto.getUsername());

        TokenResponseDto tokenResponseDto = new TokenResponseDto(token, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();

        if (jwtUtil.validateRefreshToken(refreshToken)) {
            String newAccessToken = jwtUtil.refreshToken(refreshToken);
            TokenResponseDto tokenResponseDto = new TokenResponseDto(newAccessToken, refreshToken);
            return ResponseEntity.ok(tokenResponseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
