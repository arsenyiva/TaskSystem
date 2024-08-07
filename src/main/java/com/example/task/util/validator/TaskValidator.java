package com.example.task.util.validator;

import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * Валидатор для проверки данных задач.
 */
@Component
@Validated
public class TaskValidator {
    @Autowired
    private UserService userService;

    /**
     * Проверяет данные задачи перед созданием.
     *
     * @param requestData данные задачи в виде карты ключ-значение.
     * @throws IllegalArgumentException если одно из полей неверно или отсутствует.
     */
    public void validateTask(Map<String, Object> requestData) {
        String title = (String) requestData.get("title");
        String description = (String) requestData.get("description");
        String statusStr = (String) requestData.get("status");
        String priorityStr = (String) requestData.get("priority");
        String assigneeUsername = (String) requestData.get("assignee");

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Должен быть заголовок!");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Должно быть описание!");
        }

        if (statusStr == null || statusStr.isEmpty()) {
            throw new IllegalArgumentException("Должен быть статус!");
        }

        try {
            Task.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Статус не верный!");
        }

        if (priorityStr == null || priorityStr.isEmpty()) {
            throw new IllegalArgumentException("Должен быть приоритет!");
        }

        try {
            Task.Priority.valueOf(priorityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Приоритет неверный!");
        }

        if (assigneeUsername != null && assigneeUsername.isEmpty()) {
            throw new IllegalArgumentException("Имя исполнителя не должно быть пустым!");
        }
        if (assigneeUsername != null && userService.findByUsername(assigneeUsername).isEmpty()) {
            throw new IllegalArgumentException("Неверное имя исполнителя!");
        }
    }

    /**
     * Проверяет данные задачи перед обновлением.
     *
     * @param taskDetails объект задачи с новыми данными.
     * @throws IllegalArgumentException если одно из полей неверно или отсутствует.
     */
    public void validateUpdateTask(Task taskDetails) {
        String title = taskDetails.getTitle();
        String description = taskDetails.getDescription();
        Task.Status status = taskDetails.getStatus();
        Task.Priority priority = taskDetails.getPriority();
        User assignee = taskDetails.getAssignee();

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Должен быть заголовок!");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Должно быть описание!");
        }

        if (status == null) {
            throw new IllegalArgumentException("Должен быть статус!");
        }

        if (priority == null) {
            throw new IllegalArgumentException("Должен быть приоритет!");
        }

        if (assignee != null) {
            if (userService.findByUsername(assignee.getUsername()).isEmpty()) {
                throw new IllegalArgumentException("Имя исполнителя не существует!");
            }
        }
    }
}