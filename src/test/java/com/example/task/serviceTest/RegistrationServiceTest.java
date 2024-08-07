package com.example.task.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.task.model.User;
import com.example.task.service.RegistrationService;
import com.example.task.service.UserService;
import com.example.task.util.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterNewUser_Success() {
        String username = "user";
        String password = "password";
        String encodedPassword = "encodedPassword";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        registrationService.registerNewUser(requestData);

        verify(userValidator).validateUserData(username, password, userService);

        User expectedUser = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();
        verify(userService).createUser(expectedUser);
    }

    @Test
    public void testRegisterNewUser_InvalidUserData() {
        String username = "user";
        String password = "password";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);

        doThrow(new RuntimeException("Invalid user data")).when(userValidator)
                .validateUserData(username, password, userService);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            registrationService.registerNewUser(requestData);
        });

        assertEquals("Invalid user data", thrown.getMessage());
        verify(userService, never()).createUser(any(User.class));
    }
}