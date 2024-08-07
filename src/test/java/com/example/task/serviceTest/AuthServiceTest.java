package com.example.task.serviceTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.task.service.AuthService;
import com.example.task.util.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateAndGetToken_Success() {
        String username = "user";
        String password = "password";
        String token = "jwt-token";

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        String result = authService.authenticateAndGetToken(username, password);

        assertEquals(token, result);
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtTokenUtils).generateToken(userDetails);
    }

    @Test
    public void testAuthenticateAndGetToken_Failure() {
        String username = "user";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> authService.authenticateAndGetToken(username, password));

        assertEquals("Неверное имя пользователя или пароль!", thrown.getMessage());
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(userDetailsService, never()).loadUserByUsername(username);
        verify(jwtTokenUtils, never()).generateToken(any(UserDetails.class));
    }

    @Test
    public void testAuthenticateAndGetToken_UserDetailsServiceFailure() {
        String username = "user";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new RuntimeException("User details service failure"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authService.authenticateAndGetToken(username, password));

        assertEquals("User details service failure", thrown.getMessage());
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtTokenUtils, never()).generateToken(any(UserDetails.class));
    }
}