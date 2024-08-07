package com.example.task.service;

import com.example.task.exception.custom.CommentNotFoundException;
import com.example.task.exception.custom.TaskNotFoundException;
import com.example.task.model.Comment;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.CommentRepository;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с комментариями.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Добавляет комментарий к задаче.
     *
     * @param taskId      идентификатор задачи, к которой добавляется комментарий
     * @param requestData данные комментария, включающие текст комментария
     * @throws TaskNotFoundException    если задача с указанным идентификатором не найдена
     * @throws IllegalArgumentException если текст комментария пустой или превышает 140 символов
     */
    public void addComment(Long taskId, Map<String, Object> requestData) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена id " + taskId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String content = (String) requestData.get("content");
        if (content.length() == 0 || content.length() > 140) {
            throw new IllegalArgumentException("Комментарий должен не должен быть пустым или более 140 символов!");
        }
        User author = userRepository.findByUsername(username);
        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .content(content)
                .build();
        commentRepository.save(comment);
    }

    /**
     * Получает список комментариев по идентификатору задачи.
     *
     * @param taskId идентификатор задачи
     * @return список комментариев, привязанных к задаче
     * @throws TaskNotFoundException если задача с указанным идентификатором не найдена
     */
    public List<Comment> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена id " + taskId));
        return commentRepository.findByTask(task);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария, который необходимо удалить
     * @throws CommentNotFoundException  если комментарий с указанным идентификатором не найден
     * @throws UsernameNotFoundException если текущий пользователь не найден
     */
    @SneakyThrows
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден id " + commentId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер " + username + " не найден"));
        if (!comment.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Нельзя удалять чужие комментарии!");
        }
        commentRepository.delete(comment);
    }
}
