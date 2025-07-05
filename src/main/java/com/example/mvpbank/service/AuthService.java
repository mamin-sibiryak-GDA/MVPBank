// Сервис для регистрации и аутентификации пользователей
package com.example.mvpbank.service;

import com.example.mvpbank.dto.UserLoginRequest;
import com.example.mvpbank.dto.UserRegistrationRequest;
import com.example.mvpbank.model.User;
import com.example.mvpbank.repository.UserRepository;
import com.example.mvpbank.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Указывает, что этот класс — сервисный компонент Spring
@RequiredArgsConstructor // Lombok-аннотация: создаёт конструктор для всех final-полей
public class AuthService {

    private final UserRepository userRepository;             // Репозиторий для работы с пользователями
    private final PasswordEncoder passwordEncoder;           // Кодировщик паролей
    private final AuthenticationManager authenticationManager; // Менеджер аутентификации

    // Метод регистрации пользователя
    public void register(UserRegistrationRequest request) {
        User user = User.builder()
                .username(request.getUsername()) // Устанавливаем имя пользователя
                .password(passwordEncoder.encode(request.getPassword())) // Хешируем пароль
                .email(request.getEmail()) // Устанавливаем email
                .role(request.getRole())   // Устанавливаем роль
                .build();

        userRepository.save(user); // Сохраняем пользователя в базу данных
    }

    // Метод для аутентификации пользователя и возврата User
    public User authenticate(UserLoginRequest request) {
        try {
            // Выполняем аутентификацию с помощью AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Получаем CustomUserDetails из объекта аутентификации
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Возвращаем саму сущность пользователя
            return userDetails.getUser();
        } catch (AuthenticationException e) {
            // Если аутентификация не удалась — выбрасываем исключение
            throw new RuntimeException("Неверный логин или пароль");
        }
    }
}
