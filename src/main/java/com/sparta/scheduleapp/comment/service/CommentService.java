package com.sparta.scheduleapp.comment.service;

import com.sparta.scheduleapp.comment.entity.Comment;
import com.sparta.scheduleapp.comment.repository.CommentRepository;
import com.sparta.scheduleapp.exception.CommentNotFoundException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.UnauthorizedException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
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
    @Transactional // 트랜잭션 관리
    public Comment addComment(Long scheduleId, String content, String userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));
        // 스케줄 존재 여부 확인
        Comment comment = Comment.builder()
                .content(content)
                .userId(userId)
                .schedule(schedule)
                .build(); // 새로운 댓글 객체 생성
        return commentRepository.save(comment); // 댓글 저장
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, String updatedComment, String userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }

        Comment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        comment.updateContent(updatedComment);
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }

        Comment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}