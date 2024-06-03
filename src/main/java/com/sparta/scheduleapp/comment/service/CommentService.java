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
public interface CommentService {
    Comment addComment(Long scheduleId, String content, String userId);
    Comment updateComment(Long commentId, String updatedComment, String userId);
    void deleteComment(Long commentId, String userId);
    Optional<Comment> findCommentById(Long commentId);
}