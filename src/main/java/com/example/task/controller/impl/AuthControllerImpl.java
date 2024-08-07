package com.example.task.controller.impl;


import com.example.task.controller.intrf.AuthController;
import com.example.task.model.pojo.AuthenticationRequest;
import com.example.task.model.pojo.AuthenticationResponse;
import com.example.task.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации.
 */
@RequestMapping("/auth")
@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Autowired
    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Создает JWT токен для аутентификации пользователя.
     *
     * @param authenticationRequest Запрос на аутентификацию
     * @return Ответ с JWT токеном или сообщением об ошибке
     */
    @Override
    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) {
        String token = authService.authenticateAndGetToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}