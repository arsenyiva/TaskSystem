package com.example.task.serviceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.task.model.User;
import com.example.task.repository.UserRepository;
import com.example.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);

        assertNull(result);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        userService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testIsUsernameAvailable_UsernameAvailable() {
        when(userRepository.findByUsername("availableUsername")).thenReturn(null);

        boolean result = userService.isUsernameAvailable("availableUsername");

        assertTrue(result);
    }

    @Test
    public void testIsUsernameAvailable_UsernameNotAvailable() {
        User user = new User();
        when(userRepository.findByUsername("takenUsername")).thenReturn(user);

        boolean result = userService.isUsernameAvailable("takenUsername");

        assertFalse(result);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername("username")).thenReturn(user);

        Optional<User> result = userService.findByUsername("username");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindById() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }
}