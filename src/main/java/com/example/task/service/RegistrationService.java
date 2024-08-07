package com.example.task.service;

import com.example.task.model.User;
import com.example.task.util.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис для регистрации новых пользователей
 */
@Service
public class RegistrationService {

    private final UserService userService;

    private final UserValidator userValidator;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PasswordEncoder passwordEncoder, UserService userService, UserValidator userValidator) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userValidator = userValidator;
    }

    /**
     * Регистрирует нового пользователя в системе.
     * <p>
     * Этот метод выполняет проверку предоставленных данных пользователя, такие как имя пользователя и пароль,
     * и создает нового пользователя с зашифрованным паролем.
     *
     * @param requestData карта данных, содержащая имя пользователя и пароль
     * @throws IllegalArgumentException если данные пользователя невалидны
     */
    public void registerNewUser(Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");

        userValidator.validateUserData(username, password, userService);

        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        userService.createUser(newUser);
    }
}
