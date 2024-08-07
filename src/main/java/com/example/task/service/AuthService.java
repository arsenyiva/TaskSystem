package com.example.task.service;


import com.example.task.util.JwtTokenUtils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации пользователя и генерации JWT-токена.
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtils;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Аутентифицирует пользователя и генерирует JWT-токен для него.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return JWT-токен для аутентифицированного пользователя
     */
    public String authenticateAndGetToken(String username, String password) {
        authenticate(username, password);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtils.generateToken(userDetails);
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    @SneakyThrows
    private void authenticate(String username, String password) throws AuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Неверное имя пользователя или пароль!");
        }
    }
}