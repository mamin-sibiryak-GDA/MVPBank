// Контроллер для обработки запросов на регистрацию и логин

package com.example.mvpbank.controller;

import com.example.mvpbank.dto.UserLoginRequest;
import com.example.mvpbank.dto.UserRegistrationRequest;
import com.example.mvpbank.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Помечает класс как REST-контроллер
@RequestMapping("/api/auth") // Базовый путь для всех эндпоинтов этого контроллера
@RequiredArgsConstructor // Lombok: создаёт конструктор для всех final-полей
public class AuthController {

    private final AuthService authService; // Сервис, содержащий бизнес-логику

    // Эндпоинт для регистрации пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegistrationRequest request) {
        String result = authService.register(request); // Вызываем сервис регистрации
        return ResponseEntity.ok(result); // Возвращаем результат клиенту
    }

    // Эндпоинт для логина пользователя
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request) {
        String result = authService.login(request); // Вызываем сервис логина
        return ResponseEntity.ok(result); // Возвращаем результат клиенту
    }
}
