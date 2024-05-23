package com.sparta.scheduleapp.auth.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}
