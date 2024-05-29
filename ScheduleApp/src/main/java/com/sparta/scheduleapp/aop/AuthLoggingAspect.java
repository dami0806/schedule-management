package com.sparta.scheduleapp.aop;

import com.sparta.scheduleapp.aop.entity.LoginHistory;
import com.sparta.scheduleapp.auth.entity.LoginRequest;
import com.sparta.scheduleapp.auth.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * AuthLoggingAspect: 로그인 실행 흐름 aop
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthLoggingAspect {
    private final LoginHistoryRepository loginHistoryRepository;

    /**
     * 로그인 시도 전
     * @param loginRequest: 로그인 요청
     */
    @Before("execution(* com.sparta.scheduleapp.auth.service.UserService.login(..)) && args(loginRequest))")
    public void logBeforeLoginAttempt(LoginRequest loginRequest) {
        LoginHistory loginHistory = new LoginHistory(loginRequest.getUsername(), LoginAction.LOGIN_ATTEMPT, LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);
        log.info("로그인 시도: {}", loginRequest.getUsername());
    }

    /**
     * 로그인 성공
     * @param loginRequest: 로그인 성공한 들어온 요청
     */
    @AfterReturning("execution(* com.sparta.scheduleapp.auth.service.UserService.login(..)) && args(loginRequest))")
    public void logAfterLoginSuccess(LoginRequest loginRequest) {
        LoginHistory loginHistory = new LoginHistory(loginRequest.getUsername(), LoginAction.LOGIN_SUCCESS, LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);
        log.info("로그인 성공: {}", loginRequest.getUsername());
    }

    /**
     * 로그인 실패
     * @param loginRequest: 로그인 실패한 들어온 요청
     */    @AfterThrowing("execution(* com.sparta.scheduleapp.auth.service.UserService.login(..)) && args(loginRequest))")
    public void logAfterLoginFailure(LoginRequest loginRequest) {
        loginHistoryRepository.save(new LoginHistory(loginRequest.getUsername(), LoginAction.LOGIN_FAILURE, LocalDateTime.now()));
        log.info("로그인 실패: {}", loginRequest.getUsername());
    }
}
