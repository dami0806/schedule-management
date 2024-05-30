package com.sparta.scheduleapp.aop.entity;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Schedule, Comment의 통합 crud AOP 흐름을 보기위함
 */
@Aspect
@Component
@Slf4j
public class CrudLoggingAspect {

    /**
     * Create 전 로그
     *
     * @param joinPoint:  메서드 호출 시점
     * @param requestDto: 요청 데이터
     */
    @Before("execution(* com.sparta.scheduleapp..*Service.create*(..)) && args(requestDto, ..)")
    public void logBeforeCreate(JoinPoint joinPoint, Object requestDto) {
        log.info("메서드={},요청데이터={}", joinPoint.getSignature(), requestDto);
    }

    /**
     * Create 후 로그
     * @param joinPoint : 메서드 실행 시점
     * @param responseDto: 응답 데이터
     */
    @AfterReturning(value = "execution(* com.sparta.scheduleapp..*Service.create*(..))", returning = "responseDto")
    public void logAfterCreate(JoinPoint joinPoint, Object responseDto) {
        log.info("메서드={},요청데이터={}",joinPoint.getSignature(),responseDto);
    }


    /**
     * 조회 메서드 실행 전 로깅
     * @param joinPoint 메서드 호출 시점의 메타데이터
     * @param id 조회할 ID
     */
    @Before("execution(* com.sparta.scheduleapp..*Service.get*(..)) && args(id, ..)")
    public void logBeforeRead(JoinPoint joinPoint, Long id) {
        log.info("조회 시도: 메서드={}, id={}", joinPoint.getSignature(), id);
    }

    /**
     * 조회 메서드 실행 후 로깅
     * @param joinPoint
     * @param responseDto 응답 데이터
     */
    @AfterReturning(value = "execution(* com.sparta.scheduleapp..*Service.get*(..))", returning = "responseDto")
    public void logAfterRead(JoinPoint joinPoint, Object responseDto) {
        log.info("조회 성공: 메서드={}, 응답 데이터={}", joinPoint.getSignature(), responseDto);
    }



    /**
     * CRUD 수정 메서드 실행 전 로깅
     * @param joinPoint 메서드 호출 시점의 메타데이터
     * @param id 수정할 ID
     * @param requestDto 요청 데이터
     */
    @Before("execution(* com.sparta.scheduleapp..*Service.update*(..)) && args(id, requestDto, ..)")
    public void logBeforeUpdate(JoinPoint joinPoint, Long id, Object requestDto) {
        log.info("수정 시도: 메서드={}, id={}, 요청 데이터={}", joinPoint.getSignature(), id, requestDto);
    }

    /**
     * 수정 메서드 실행 후 로깅
     * @param joinPoint 메서드 호출 시점의 메타데이터
     * @param responseDto 응답 데이터
     */
    @AfterReturning(value = "execution(* com.sparta.scheduleapp..*Service.update*(..))", returning = "responseDto")
    public void logAfterUpdate(JoinPoint joinPoint, Object responseDto) {
        log.info("수정 성공: 메서드={}, 응답 데이터={}", joinPoint.getSignature(), responseDto);
    }
    /**
     * 삭제 메서드 실행 전 로깅
     * @param joinPoint 메서드 호출 시점의 메타데이터
     * @param id 삭제할 ID
     */
    @Before("execution(* com.sparta.scheduleapp..*Service.delete*(..)) && args(id, ..)")
    public void logBeforeDelete(JoinPoint joinPoint, Long id) {
        log.info("삭제 시도: 메서드={}, id={}", joinPoint.getSignature(), id);
    }

    /**
     * 삭제 메서드 실행 후 로깅
     * @param joinPoint 메서드 호출 시점의 메타데이터
     * @param result 결과 데이터
     */
    @AfterReturning(value = "execution(* com.sparta.scheduleapp..*Service.delete*(..))", returning = "result")
    public void logAfterDelete(JoinPoint joinPoint, Object result) {
        log.info("삭제 성공: 메서드={}, 결과={}", joinPoint.getSignature(), result);
    }
}