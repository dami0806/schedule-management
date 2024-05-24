package com.sparta.scheduleapp.auth.service;


import com.sparta.scheduleapp.auth.dto.LoginRequestDto;
import com.sparta.scheduleapp.auth.dto.SignupRequestDto;
import com.sparta.scheduleapp.auth.entity.LoginRequest;
import com.sparta.scheduleapp.auth.entity.SignupRequest;
import com.sparta.scheduleapp.auth.entity.User;
import com.sparta.scheduleapp.auth.entity.UserRoleEnum;
import com.sparta.scheduleapp.auth.repository.UserRepository;
import com.sparta.scheduleapp.auth.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 과제에 맞춰서 암호화 안썼는데 과제제출 후 다시 쓰고싶어서 남겨둠

    @Autowired
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "e36f112d-c6f2-466f-aad8-14dcdc16360b";

    // 회원가입
    public void signup(SignupRequest signupRequest) {
        String username = signupRequest.getUsername();

        // username 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)
        if(!username.matches("^[a-z0-9]{4,10}$")) {
            throw new IllegalArgumentException("아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z)와 숫자(0~9)로 구성되어야 합니다.");

        }

        String password = signupRequest.getPassword();

        // password  최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
        if(!signupRequest.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$")) {
            throw new IllegalArgumentException("비밀번호은 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z)와 숫자(0~9)로 구성되어야 합니다.");
        }
        // 비밀번호 인코딩 암호화했다가 조건에서 풀라함
    //    String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = signupRequest.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequest.isAdmin()) {
            if (!ADMIN_TOKEN.equals(signupRequest.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 새로운 사용자 객체 생성 - 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
        logger.info("회원가입 성공: " + user.getUsername());
    }
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("이름과 비밀번호가 일치하지 않습니다."));

//        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
        // 평문끼리 비교
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createAccessToken(user.getUsername());
        logger.info("로그인 성공: 사용자 {}, 토큰 {}", user.getUsername(), token);
        return token;
    }


}