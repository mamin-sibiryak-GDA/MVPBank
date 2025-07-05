// Контроллер для аутентификации и регистрации пользователей
package com.example.mvpbank.controller;

import com.example.mvpbank.dto.UserLoginRequest;
import com.example.mvpbank.dto.UserRegistrationRequest;
import com.example.mvpbank.model.User;
import com.example.mvpbank.security.JwtUtil;
import com.example.mvpbank.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Указывает, что этот класс — REST-контроллер
@RequestMapping("/api/auth") // Базовый путь для всех эндпоинтов внутри этого контроллера
@RequiredArgsConstructor // Lombok-аннотация: генерирует конструктор для final полей
public class AuthController {

    private final AuthService authService; // Сервис, отвечающий за регистрацию и логин
    private final JwtUtil jwtUtil;         // Утилита для генерации JWT токена

    @PostMapping("/register") // Эндпоинт для регистрации нового пользователя
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest request) {
        try {
            authService.register(request); // Пытаемся зарегистрировать
            return ResponseEntity.ok("Регистрация прошла успешно"); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка регистрации: " + e.getMessage()); // 400 Bad Request
        }
    }

    @PostMapping("/login") // Эндпоинт для входа в систему
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        try {
            User user = authService.authenticate(request); // Пытаемся аутентифицировать
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole()); // Генерируем токен
            return ResponseEntity.ok(token); // 200 OK и токен в теле
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Ошибка входа: " + e.getMessage()); // 401 Unauthorized
        }
    }
}

