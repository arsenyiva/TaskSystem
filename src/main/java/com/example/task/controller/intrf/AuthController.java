package com.example.task.controller.intrf;

import com.example.task.model.pojo.AuthenticationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Интерфейс для аутентификации пользователя и генерации JWT токена.
 * Определяет операции для работы с пользователями, включая аутентификацию.
 */
public interface AuthController {

    @PostMapping
    @Operation(summary = "Аутентификация пользователя и генерация JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest);
}