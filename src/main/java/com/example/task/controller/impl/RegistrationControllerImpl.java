package com.example.task.controller.impl;


import com.example.task.controller.intrf.RegistrationController;
import com.example.task.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер для регистрации новых пользователей.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationControllerImpl implements RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationControllerImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Регистрирует нового пользователя.
     * Метод принимает данные нового пользователя и выполняет регистрацию в системе.
     *
     * @param requestData данные нового пользователя в виде карты, содержащие информацию о регистрации
     * @return ответ с сообщением об успешной регистрации пользователя
     */
    @Override
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> requestData) {
        registrationService.registerNewUser(requestData);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }
}