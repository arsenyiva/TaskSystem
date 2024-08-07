package com.example.task.controller.impl;

import com.example.task.controller.intrf.TaskController;
import com.example.task.model.Task;
import com.example.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления задачами.
 */
@RestController
@RequestMapping("/tasks")
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Создает новую задачу.
     * Принимает данные задачи и создаёт её в системе.
     *
     * @param requestData данные задачи в виде карты
     * @return ответ с сообщением об успешном создании задачи
     */
    @Override
    public ResponseEntity<String> createTask(@RequestBody Map<String, Object> requestData) {
        taskService.createTask(requestData);
        return ResponseEntity.status(HttpStatus.CREATED).body("Задача добавлена успешно!");
    }

    /**
     * Частично обновляет задачу по ID.
     * Принимает ID задачи и новые данные для обновления.
     *
     * @param id          идентификатор задачи
     * @param taskDetails новые данные задачи
     * @return ответ с сообщением об успешном обновлении задачи
     */
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> taskDetails) {
        taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok("Задача успешно обновлена!");
    }

    /**
     * Получает задачу по ID.
     * Возвращает задачу с указанным ID.
     *
     * @param id идентификатор задачи
     * @return ответ с задачей
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Получает все задачи.
     * Возвращает список всех задач в системе.
     *
     * @return ответ со списком задач
     */

    @Override
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Удаляет задачу по ID.
     * Удаляет задачу с указанным ID.
     *
     * @param id идентификатор задачи
     * @return ответ с сообщением об успешном удалении задачи
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Успешно удалено");
    }

    /**
     * Обновляет статус задачи по ID.
     * Принимает ID задачи и новые данные для изменения статуса.
     *
     * @param id          идентификатор задачи
     * @param taskDetails новые данные для обновления статуса задачи
     * @return ответ с сообщением об успешном изменении статуса задачи
     */
    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long id, @RequestBody Map<String, Object> taskDetails) {
        taskService.changeStatus(id, taskDetails);
        return ResponseEntity.ok("Статус успешно изменен!");
    }

    /**
     * Получает задачи по автору с пагинацией и фильтрацией.
     * Возвращает список задач, созданных указанным автором, с возможностью пагинации и фильтрации по статусу и приоритету.
     *
     * @param authorId идентификатор автора
     * @param page     номер страницы (по умолчанию 0)
     * @param size     размер страницы (по умолчанию 10)
     * @param status   статус задачи для фильтрации (необязательно)
     * @param priority приоритет задачи для фильтрации (необязательно)
     * @return ответ со списком задач
     */
    @Override
    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getTasksByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority) {

        return taskService.getTasksByAuthor(authorId, status, priority, page, size);
    }

    /**
     * Получает задачи по исполнителю с пагинацией и фильтрацией.
     * Возвращает список задач, назначенных указанному исполнителю, с возможностью пагинации и фильтрации по статусу и приоритету.
     *
     * @param assigneeId идентификатор исполнителя
     * @param page       номер страницы (по умолчанию 0)
     * @param size       размер страницы (по умолчанию 10)
     * @param status     статус задачи для фильтрации (необязательно)
     * @param priority   приоритет задачи для фильтрации (необязательно)
     * @return ответ со списком задач
     */
    @Override
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<?> getTasksByAssignee(
            @PathVariable Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority) {

        return taskService.getTasksByAssignee(assigneeId, status, priority, page, size);
    }
}