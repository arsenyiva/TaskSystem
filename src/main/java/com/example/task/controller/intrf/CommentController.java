package com.example.task.controller.intrf;


import com.example.task.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для работы с комментариями задач.
 * Определяет операции для добавления, получения и удаления комментариев.
 */
public interface CommentController {


    @Operation(summary = "Добавление комментария к задаче")
    @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<String> addComment(Long taskId, Map<String, Object> requestData);


    @Operation(summary = "Получение всех комментариев по задаче")
    @ApiResponse(responseCode = "200", description = "Комментарии успешно получены")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<List<Comment>> getCommentsByTask(Long taskId);


    @Operation(summary = "Удаление комментария")
    @ApiResponse(responseCode = "200", description = "Комментарий успешно удален")
    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    ResponseEntity<String> deleteComment(Long commentId);
}