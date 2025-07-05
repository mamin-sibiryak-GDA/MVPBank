package com.example.mvpbank.exception;

// Кастомное исключение при регистрации, если пользователь уже существует
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
