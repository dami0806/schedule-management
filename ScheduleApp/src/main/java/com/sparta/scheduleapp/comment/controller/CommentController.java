package com.sparta.scheduleapp.comment.controller;

import com.sparta.scheduleapp.comment.dto.CommentRequestDto;
import com.sparta.scheduleapp.comment.dto.CommentResponseDto;
import com.sparta.scheduleapp.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // 댓글 작성
    @PostMapping("{scheduleId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto requestDto,@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(commentService.addComment(scheduleId, requestDto, userId));
    }

    // 댓글 수정
    @PutMapping("comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(commentService.updateComment(commentId, requestDto, userId));
    }

    // 댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}