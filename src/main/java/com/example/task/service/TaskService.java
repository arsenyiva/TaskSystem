package com.example.task.service;

import com.example.task.exception.custom.TaskNotFoundException;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.TaskRepository;
import com.example.task.util.validator.TaskValidator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с задачами.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final TaskValidator taskValidator;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, TaskValidator taskValidator) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.taskValidator = taskValidator;
    }

    /**
     * Создает новую задачу.
     * Этот метод выполняет проверку данных задачи и создает новую задачу с указанными
     * заголовком, описанием, статусом, приоритетом, автором и, при наличии, исполнителем.
     *
     * @param requestData карта данных задачи, включая заголовок, описание, статус, приоритет и исполнителя
     * @throws IllegalArgumentException  если данные задачи некорректны
     * @throws UsernameNotFoundException если автор или исполнитель не найдены
     */
    public void createTask(Map<String, Object> requestData) {
        taskValidator.validateTask(requestData);

        String title = (String) requestData.get("title");
        String description = (String) requestData.get("description");

        String statusStr = (String) requestData.get("status");
        Task.Status status = Task.Status.valueOf(statusStr.toUpperCase());

        String priorityStr = (String) requestData.get("priority");
        Task.Priority priority = Task.Priority.valueOf(priorityStr.toUpperCase());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер с именем " + username + " не найден"));

        String assigneeUsername = (String) requestData.get("assignee");
        User assignee = (assigneeUsername != null) ? userService.findByUsername(assigneeUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер с именем " + assigneeUsername + " не найден")) : null;

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .author(author)
                .assignee(assignee)
                .build();

        taskRepository.save(task);
    }

    /**
     * Обновляет существующую задачу.
     * Этот метод выполняет частичное обновление полей задачи. Проверяется, что текущий пользователь
     * является автором задачи перед её обновлением.
     *
     * @param id          идентификатор задачи, которую нужно обновить
     * @param taskDetails карта данных для обновления задачи, включая заголовок, описание, статус, приоритет и исполнителя
     */
    @SneakyThrows
    public void updateTask(Long id, Map<String, Object> taskDetails) {
        Task task = getTaskById(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер с именем " + username + " не найден"));

        if (!task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Нельзя менять чужие задачи!");
        }

        Task updatedTask = Task.builder()
                .title((String) taskDetails.get("title"))
                .description((String) taskDetails.get("description"))
                .status(taskDetails.containsKey("status") ? Task.Status.valueOf(((String) taskDetails.get("status")).toUpperCase()) : task.getStatus())
                .priority(taskDetails.containsKey("priority") ? Task.Priority.valueOf(((String) taskDetails.get("priority")).toUpperCase()) : task.getPriority())
                .assignee(taskDetails.containsKey("assignee") ? userService.findByUsername((String) taskDetails.get("assignee"))
                        .orElseThrow(() -> new IllegalArgumentException("Такого исполнителя не существует.")) : task.getAssignee())
                .build();

        taskValidator.validateUpdateTask(updatedTask);

        if (taskDetails.containsKey("title")) {
            task.setTitle(updatedTask.getTitle());
        }

        if (taskDetails.containsKey("description")) {
            task.setDescription(updatedTask.getDescription());
        }

        if (taskDetails.containsKey("status")) {
            task.setStatus(updatedTask.getStatus());
        }

        if (taskDetails.containsKey("priority")) {
            task.setPriority(updatedTask.getPriority());
        }

        if (taskDetails.containsKey("assignee")) {
            task.setAssignee(updatedTask.getAssignee());
        }

        taskRepository.save(task);
    }

    /**
     * Получает задачу по её идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача с указанным идентификатором
     * @throws TaskNotFoundException если задача с указанным идентификатором не найдена
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена id " + id));
    }

    /**
     * Получает все задачи.
     *
     * @return список всех задач
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Удаляет задачу по её идентификатору.
     * Этот метод проверяет, что текущий пользователь является автором задачи перед её удалением.
     *
     * @param id идентификатор задачи, которую нужно удалить
     * @throws TaskNotFoundException     если задача с указанным идентификатором не найдена
     * @throws UsernameNotFoundException если текущий пользователь не найден
     */
    @SneakyThrows
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер с именем " + username + " не найден"));
        if (!task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Нельзя удалять чужие задачи!");
        }
        taskRepository.delete(task);
    }

    /**
     * Изменяет статус задачи.
     * Этот метод проверяет, что текущий пользователь является автором или исполнителем задачи перед
     * изменением её статуса.
     *
     * @param id          идентификатор задачи, для которой нужно изменить статус
     * @param taskDetails карта данных, содержащая новый статус задачи
     * @throws TaskNotFoundException     если задача с указанным идентификатором не найдена
     * @throws UsernameNotFoundException если текущий пользователь не найден
     * @throws IllegalArgumentException  если значение статуса некорректно
     */
    @SneakyThrows
    public void changeStatus(Long id, Map<String, Object> taskDetails) {
        Task task = getTaskById(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер с именем " + username + " не найден"));

        if (!task.getAssignee().equals(currentUser) || !task.getAuthor().equals(currentUser)) {
            throw new AccessDeniedException("Нельзя менять статус чужих задач!");
        }

        String statusStr = (String) taskDetails.get("status");
        Task.Status status;
        try {
            status = Task.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверное значение статуса!");
        }
        task.setStatus(status);
        taskRepository.save(task);
    }

    /**
     * Получает задачи по автору с возможностью фильтрации по статусу и приоритету.
     *
     * @param authorId идентификатор автора задач
     * @param status   статус задачи (может быть null)
     * @param priority приоритет задачи (может быть null)
     * @param page     номер страницы для пагинации
     * @param size     количество задач на странице
     * @return список задач, соответствующих критериям, или сообщение об ошибке, если задачи не найдены
     * @throws UsernameNotFoundException если автор с указанным идентификатором не найден
     */
    public ResponseEntity<?> getTasksByAuthor(Long authorId, Task.Status status, Task.Priority priority, int page, int size) {
        userService.findById(authorId)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер не найден id " + authorId));
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByAuthorIdAndStatusAndPriority(authorId, status, priority, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByAuthorIdAndStatus(authorId, status, pageable);
        } else if (priority != null) {
            tasks = taskRepository.findByAuthorIdAndPriority(authorId, priority, pageable);
        } else {
            tasks = taskRepository.findByAuthorId(authorId, pageable);
        }

        if (tasks.isEmpty()) {
            String message = (status != null && priority != null)
                    ? "Задач у автора с таким статусом и приоритетом не найдено"
                    : (status != null ? "Задач у автора с таким статусом не найдено"
                    : (priority != null ? "Задач у автора с таким приоритетом не найдено"
                    : "Задач у автора не найдено"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(tasks);
    }

    /**
     * Получает задачи по исполнителю с возможностью фильтрации по статусу и приоритету.
     *
     * @param assigneeId идентификатор исполнителя задач
     * @param status     статус задачи (может быть null)
     * @param priority   приоритет задачи (может быть null)
     * @param page       номер страницы для пагинации
     * @param size       количество задач на странице
     * @return список задач, соответствующих критериям, или сообщение об ошибке, если задачи не найдены
     * @throws UsernameNotFoundException если исполнитель с указанным идентификатором не найден
     */
    public ResponseEntity<?> getTasksByAssignee(Long assigneeId, Task.Status status, Task.Priority priority, int page, int size) {
        userService.findById(assigneeId)
                .orElseThrow(() -> new UsernameNotFoundException("Юзер не найден id " + assigneeId));
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByAssigneeIdAndStatusAndPriority(assigneeId, status, priority, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByAssigneeIdAndStatus(assigneeId, status, pageable);
        } else if (priority != null) {
            tasks = taskRepository.findByAssigneeIdAndPriority(assigneeId, priority, pageable);
        } else {
            tasks = taskRepository.findByAssigneeId(assigneeId, pageable);
        }

        if (tasks.isEmpty()) {
            String message = (status != null && priority != null)
                    ? "Задач у исполнителя с таким статусом и приоритетом не найдено"
                    : (status != null ? "Задач у исполнителя с таким статусом не найдено"
                    : (priority != null ? "Задач у исполнителя с таким приоритетом не найдено"
                    : "Задач у исполнителя не найдено"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(tasks);
    }
}
