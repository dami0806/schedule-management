package com.sparta.scheduleapp.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDto {
    @Schema(description = "할일 제목", example = "제목은 필수입니다", required = true)
    @NotBlank(message = "할일 제목 입력은 필수입니다.")
    @Size(max = 200, message = "할일 제목은 최대 200자입니다.")
    private String title;

    @Schema(description = "할일 내용")
    private String description;

    @Schema(description = "담당자 이메일", example = "user@example.com", required = true)
    @NotBlank(message = "담당자 작성은 필수입니다.")
    @Email(message = "담당자 작성은 email 형식이어야 합니다.")
    private String assignee;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    private String password;

    @Schema(description = "날짜", example = "2023-05-16", required = true)
    @NotBlank(message = "날짜 입력은 필수입니다.")
    private String date;
}
