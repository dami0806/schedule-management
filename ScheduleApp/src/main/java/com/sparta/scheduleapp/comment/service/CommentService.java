package com.sparta.scheduleapp.comment.service;

import com.sparta.scheduleapp.comment.dto.CommentRequestDto;
import com.sparta.scheduleapp.comment.dto.CommentResponseDto;
import com.sparta.scheduleapp.comment.entity.Comment;
import com.sparta.scheduleapp.comment.repository.CommentRepository;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // 댓글 추가
    public CommentResponseDto addComment(Long scheduleId, CommentRequestDto requestDto, String userId) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        if (scheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("일정을 찾을 수 없습니다.");
        }

        Schedule schedule = scheduleOpt.get();
        Comment comment = new Comment(requestDto.getContent(), userId, schedule);
        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getUserId(), comment.getCreatedAt());
    }

    // 댓글 수정
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        Comment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        comment.updateContent(requestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getContent(), comment.getUserId(), comment.getCreatedAt());
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다.");
        }

        Comment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}