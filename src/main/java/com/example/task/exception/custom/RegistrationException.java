package com.example.task.exception.custom;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}