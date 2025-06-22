// Сервис для бизнес-логики аутентификации и регистрации

package com.example.mvpbank.service;

import com.example.mvpbank.dto.UserLoginRequest;
import com.example.mvpbank.dto.UserRegistrationRequest;
import com.example.mvpbank.model.User;
import com.example.mvpbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Помечает класс как сервисный компонент Spring
@RequiredArgsConstructor // Lombok: создаёт конструктор для всех final полей
public class AuthService {

    private final UserRepository userRepository; // Для доступа к данным пользователей
    private final PasswordEncoder passwordEncoder; // Для хеширования паролей
    private final AuthenticationManager authenticationManager; // Для выполнения логина

    // Метод регистрации пользователя
    public String register(UserRegistrationRequest request) {
        // Проверка, существует ли пользователь с таким username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Пользователь уже существует";
        }

        // Создание нового пользователя с данными из запроса
        User user = User.builder()
                .username(request.getUsername()) // Устанавливаем логин
                .password(passwordEncoder.encode(request.getPassword())) // Хешируем пароль
                .email(request.getEmail()) // Устанавливаем email
                .role(request.getRole()) // Устанавливаем роль
                .build();

        // Сохраняем пользователя в базе данных
        userRepository.save(user);

        return "Регистрация прошла успешно";
    }

    // Метод логина пользователя
    public String login(UserLoginRequest request) {
        // Аутентификация с помощью переданных логина и пароля
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Проверка успешности аутентификации
        if (authentication.isAuthenticated()) {
            return "Вы успешно вошли в систему";
        } else {
            return "Неверные учетные данные";
        }
    }
}
