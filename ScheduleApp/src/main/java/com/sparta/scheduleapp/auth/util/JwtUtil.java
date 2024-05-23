package com.sparta.scheduleapp.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

// JwtUtil : - JWT 토큰을 생성하고 검증
@Component
public class JwtUtil {
    private final String secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Value("${jwt.token.expiration}")
    private long tokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    // 액세스 토큰 생성
    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 토큰 주체
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        // 토큰에서 사용자 이름 추출
        final String username = getUsernameFromToken(token);
        // 토큰 검사(이름 일치, 만료 확인)
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    // 리프레시토큰 유효성 검사
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리프레시 토큰으로 토큰 재발급 - 엑세스 토큰 만료시
    public String refreshToken(String refreshToken) {
        // 리프레시 유효
        if (validateRefreshToken(refreshToken)) {
            String username = getUsernameFromToken(refreshToken);
            return createToken(username);
        } else {
            // 이때 프론트가 적절히 로그인으로 유도
            throw new IllegalArgumentException("Refresh token이 만료 또는 유효하지 않음");
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 사용자 추출
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }
}
