package com.sparta.scheduleapp.comment.controller;

import com.sparta.scheduleapp.comment.dto.CommentRequestDto;
import com.sparta.scheduleapp.comment.dto.CommentResponseDto;
import com.sparta.scheduleapp.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "CommentController", description = "댓글 API")

public class CommentController {
    @Autowired
    private CommentService commentService;

    // 댓글 작성
    @PostMapping("{scheduleId}/comments")
    @Operation(summary = "댓글 작성", description = "새로운 댓글을 추가합니다.")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto requestDto,@AuthenticationPrincipal UserDetails userDetails) {
        // 인증된 사용자의 이름을 가져옴
        String userId = userDetails.getUsername();
        CommentResponseDto responseDto = new CommentResponseDto(commentService.addComment(scheduleId, requestDto.getContent(), userId));
        // 생성된 댓글을 포함한 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 수정
    @PutMapping("comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(commentService.updateComment(commentId, requestDto, userId));
    }

    // 댓글 삭제
    @DeleteMapping("comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}