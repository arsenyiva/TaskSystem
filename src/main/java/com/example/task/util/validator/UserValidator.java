package com.example.task.util.validator;


import com.example.task.exception.custom.RegistrationException;
import com.example.task.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Валидатор для проверки данных пользователей.
 */
@Component
@Validated
public class UserValidator {

    /**
     * Проверяет данные пользователя, такие как имя пользователя и пароль.
     *
     * @param username    имя пользователя.
     * @param password    пароль пользователя.
     * @param userService сервис для проверки доступности имени пользователя.
     * @throws RegistrationException если имя пользователя уже занято или данные не соответствуют требованиям.
     */
    public void validateUserData(String username, String password, UserService userService) {
        checkString(username, "Username", 3, 30);
        checkString(password, "Password", 6, 50);

        if (!userService.isUsernameAvailable(username)) {
            throw new RegistrationException("Имя пользователя уже занято");
        }
    }

    /**
     * Проверяет строковое значение на пустоту и длину.
     *
     * @param value     значение для проверки.
     * @param fieldName имя поля для использования в сообщении об ошибке.
     * @param minLength минимальная длина строки.
     * @param maxLength максимальная длина строки.
     * @throws RegistrationException если строка пустая или не соответствует требованиям по длине.
     */
    private void checkString(String value, String fieldName, int minLength, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            throw new RegistrationException(fieldName + " не должно быть пустым");
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new RegistrationException(fieldName + " Должно быть не менее " + minLength + " и не более " + maxLength);
        }
    }
}
