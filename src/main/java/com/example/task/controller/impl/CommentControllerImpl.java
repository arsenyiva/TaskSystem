package com.example.task.controller.impl;

import com.example.task.controller.intrf.CommentController;
import com.example.task.model.Comment;
import com.example.task.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с комментариями.
 * Обеспечивает операции для добавления, получения и удаления комментариев к задачам.
 */
@RestController
@RequestMapping("/tasks")
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Добавляет комментарий к задаче.
     * Метод принимает идентификатор задачи и данные комментария и добавляет комментарий к указанной задаче.
     *
     * @param taskId      идентификатор задачи, к которой добавляется комментарий
     * @param requestData данные комментария
     * @return ответ с сообщением об успешном добавлении комментария
     */
    @Override
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long taskId, @RequestBody Map<String, Object> requestData) {
        commentService.addComment(taskId, requestData);
        return ResponseEntity.status(HttpStatus.CREATED).body("Комментарий успешно добавлен!");
    }

    /**
     * Получает все комментарии по задаче.
     * Метод возвращает список всех комментариев, связанных с указанной задачей.
     *
     * @param taskId идентификатор задачи, для которой необходимо получить комментарии
     * @return ответ со списком комментариев
     */
    @Override
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTask(@PathVariable Long taskId) {
        List<Comment> comments = commentService.getCommentsByTask(taskId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Удаляет комментарий по идентификатору.
     * Метод удаляет комментарий, идентифицируемый переданным идентификатором.
     *
     * @param commentId идентификатор комментария, который необходимо удалить
     * @return ответ с сообщением об успешном удалении комментария
     */
    @Override
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Комментарий успешно удален!");
    }
}
