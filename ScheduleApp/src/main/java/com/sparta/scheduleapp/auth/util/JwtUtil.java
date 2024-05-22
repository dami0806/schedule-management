package com.sparta.scheduleapp.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    @Value("${jwt.token.expiration}")
    private long tokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    //엑세스 토큰 만료 기간
    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

  // 리프레시 토큰 기간
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

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
