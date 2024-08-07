package com.example.task.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.task.exception.custom.CommentNotFoundException;
import com.example.task.exception.custom.TaskNotFoundException;
import com.example.task.model.Comment;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.CommentRepository;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import com.example.task.service.CommentService;
import com.example.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddComment_TaskNotFound() {
        Long taskId = 1L;
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("content", "This is a comment");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException thrown = assertThrows(TaskNotFoundException.class, () -> commentService.addComment(taskId, requestData));

        assertEquals("Задача не найдена id " + taskId, thrown.getMessage());
    }

    @Test
    public void testGetCommentsByTask_Success() {
        Long taskId = 1L;
        Task task = new Task();
        List<Comment> comments = List.of(new Comment(), new Comment());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findByTask(task)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByTask(taskId);

        assertEquals(comments, result);
    }

    @Test
    public void testGetCommentsByTask_TaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException thrown = assertThrows(TaskNotFoundException.class, () -> commentService.getCommentsByTask(taskId));

        assertEquals("Задача не найдена id " + taskId, thrown.getMessage());
    }

    @Test
    public void testDeleteComment_Success() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setAuthor(new User());

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userService.findByUsername("username")).thenReturn(Optional.of(comment.getAuthor()));
        doNothing().when(commentRepository).delete(any(Comment.class));

        commentService.deleteComment(commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    public void testDeleteComment_CommentNotFound() {
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentNotFoundException thrown = assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(commentId));

        assertEquals("Комментарий не найден id " + commentId, thrown.getMessage());
    }
}