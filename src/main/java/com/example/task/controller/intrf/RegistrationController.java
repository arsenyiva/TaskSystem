package com.example.task.controller.intrf;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Интерфейс для регистрации пользователей.
 * Определяет операцию для регистрации нового пользователя.
 */
public interface RegistrationController {

    @Operation(summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя с указанными данными.")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Невозможно зарегистрировать пользователя, имя пользователя уже занято")
    ResponseEntity<String> registerUser(@RequestBody Map<String, Object> requestData);
}