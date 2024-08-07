package com.example.task.controller.intrf;

import com.example.task.model.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для управления задачами.
 * Определяет операции для создания, обновления, получения, удаления задач и их фильтрации.
 */
public interface TaskController {

    @PostMapping
    @Operation(summary = "Создание новой задачи",
            description = "Создает новую задачу с указанными данными.")
    @ApiResponse(responseCode = "201", description = "Задача успешно создана")
    ResponseEntity<String> createTask(@RequestBody Map<String, Object> requestData);


    @Operation(summary = "Частичное обновление задачи по ID",
            description = "Обновляет частично или полностью задачу по ID.")
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Map<String, Object> taskDetails);


    @Operation(summary = "Получение задачи по ID",
            description = "Возвращает задачу по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Задача успешно найдена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<Task> getTaskById(@PathVariable Long id);

    @GetMapping
    @Operation(summary = "Получение всех задач",
            description = "Возвращает список всех задач.")
    @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    ResponseEntity<List<Task>> getAllTasks();


    @Operation(summary = "Удаление задачи по ID",
            description = "Удаляет задачу по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Задача успешно удалена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<String> deleteTask(@PathVariable Long id);


    @Operation(summary = "Обновление статуса задачи по ID",
            description = "Обновляет статус задачи по указанному ID.")
    @ApiResponse(responseCode = "200", description = "Статус задачи успешно изменен")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    ResponseEntity<String> changeStatus(@PathVariable Long id, @RequestBody Map<String, Object> taskDetails);


    @Operation(summary = "Получение задач по автору с пагинацией и фильтрацией",
            description = "Возвращает задачи, созданные указанным автором, с возможностью пагинации и фильтрации.")
    @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    @ApiResponse(responseCode = "404", description = "Задачи не найдены")
    ResponseEntity<?> getTasksByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority);


    @Operation(summary = "Получение задач по исполнителю с пагинацией и фильтрацией",
            description = "Возвращает задачи, назначенные указанному исполнителю, с возможностью пагинации и фильтрации.")
    @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    @ApiResponse(responseCode = "404", description = "Задачи не найдены")
    ResponseEntity<?> getTasksByAssignee(
            @PathVariable Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false) Task.Priority priority);
}