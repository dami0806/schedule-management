package com.sparta.scheduleapp.auth.controller;

import com.sparta.scheduleapp.auth.dto.LoginRequestDto;
import com.sparta.scheduleapp.auth.dto.SignupRequestDto;
import com.sparta.scheduleapp.auth.dto.TokenResponseDto;
import com.sparta.scheduleapp.auth.entity.LoginRequest;
import com.sparta.scheduleapp.auth.entity.SignupRequest;
import com.sparta.scheduleapp.auth.service.UserService;
import com.sparta.scheduleapp.auth.util.JwtUtil;
import com.sparta.scheduleapp.comment.dto.RefreshTokenRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth API")
public class AuthRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthRestController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원가입", description = "회원데이터 서버에 저장")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        SignupRequest signupRequest = new SignupRequest(
                signupRequestDto.getUsername(),
                signupRequestDto.getPassword(),
                signupRequestDto.getEmail(),
                signupRequestDto.isAdmin(),
                signupRequestDto.getAdminToken()
        );
        userService.signup(signupRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "토큰값 생성")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginRequest loginRequest = new LoginRequest(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );

        String token = userService.login(loginRequest);
        String refreshToken = jwtUtil.createRefreshToken(loginRequestDto.getUsername());

        TokenResponseDto tokenResponseDto = new TokenResponseDto(token, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @Operation(summary = "토큰값 갱신", description = "Refresh토큰으로 토큰값 갱신")
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
