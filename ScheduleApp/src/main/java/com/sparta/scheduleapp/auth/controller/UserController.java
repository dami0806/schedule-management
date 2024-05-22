//package com.sparta.scheduleapp.auth.Controller;
//
//
//import com.sparta.scheduleapp.auth.dto.LoginRequestDto;
//import com.sparta.scheduleapp.auth.dto.SignupRequestDto;
//import com.sparta.scheduleapp.auth.dto.TokenResponseDto;
//import com.sparta.scheduleapp.auth.service.UserService;
//import com.sparta.scheduleapp.auth.util.JwtUtil;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//@RestController
//@RequestMapping("/api/auth")
//public class UserController {
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    public UserController(UserService userService, JwtUtil jwtUtil) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//    }
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//    @GetMapping("/signup")
//    public String signupPage() {
//        return "signup";
//    }
//
//    @PostMapping("/signup")
//    public String signup(@RequestBody SignupRequestDto requestDto) {
//        userService.signup(requestDto);
//        ModelAndView modelAndView = new ModelAndView("redirect:/api/auth/login");
//        modelAndView.addObject("signupSuccess", true);
//        return "redirect:/api/auth/login"; // 로그인 페이지로 리다이렉트
//    }
//  // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//        String token = userService.login(loginRequestDto);
//        String refreshToken = jwtUtil.createRefreshToken(loginRequestDto.getUsername());
//
//        TokenResponseDto tokenResponseDto = new TokenResponseDto(token, refreshToken);
//        return ResponseEntity.ok(tokenResponseDto);
//    }
//}