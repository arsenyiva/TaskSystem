package com.example.task.serviceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.repository.TaskRepository;
import com.example.task.service.TaskService;
import com.example.task.service.UserService;
import com.example.task.util.validator.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskValidator taskValidator;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private User mockUser;

    @Mock
    private Task mockTask;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("user"); // Настройка для всех тестов
    }

    @Test
    public void testCreateTask() {
        Map<String, Object> requestData = Map.of(
                "title", "Test Task",
                "description", "Test Description",
                "status", "COMPLETED",
                "priority", "HIGH",
                "assignee", "assigneeUser"
        );

        when(userService.findByUsername("assigneeUser")).thenReturn(Optional.of(mockUser));
        when(userService.findByUsername("user")).thenReturn(Optional.of(mockUser)); // Настройка для текущего пользователя

        taskService.createTask(requestData);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        Map<String, Object> taskDetails = Map.of(
                "title", "Updated Title",
                "status", "COMPLETED"
        );

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(userService.findByUsername("user")).thenReturn(Optional.of(mockUser));
        when(mockTask.getAuthor()).thenReturn(mockUser);
        when(mockTask.getAssignee()).thenReturn(mockUser);

        taskService.updateTask(1L, taskDetails);

        verify(taskRepository, times(1)).save(mockTask);
    }

    @Test
    public void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        when(userService.findByUsername("user")).thenReturn(Optional.of(mockUser));
        when(mockTask.getAuthor()).thenReturn(mockUser);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(mockTask);
    }

    @Test
    public void testGetTasksByAuthor() {
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.findByAuthorId(1L, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(mockTask)));

        ResponseEntity<?> response = taskService.getTasksByAuthor(1L, null, null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTasksByAssignee() {
        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(taskRepository.findByAssigneeId(1L, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(mockTask)));

        ResponseEntity<?> response = taskService.getTasksByAssignee(1L, null, null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}