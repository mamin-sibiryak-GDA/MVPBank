package com.example.mvpbank.dto;

import jakarta.validation.constraints.Email;           // Проверка, что строка — корректный email
import jakarta.validation.constraints.NotBlank;       // Проверка, что строка не пустая
import jakarta.validation.constraints.Size;           // Проверка минимального/максимального размера строки

import lombok.Getter; // Автоматическая генерация геттера
import lombok.Setter; // Автоматическая генерация сеттера
import lombok.NoArgsConstructor; // Генерация конструктора без аргументов
import lombok.AllArgsConstructor; // Генерация конструктора со всеми аргументами

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 4, max = 20, message = "Имя пользователя должно быть от 4 до 20 символов")
    private String username; // Имя пользователя, которое будет использоваться при логине

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email; // Адрес электронной почты (будет уникальным)

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть минимум 6 символов")
    private String password; // Пароль, который будет зашифрован и сохранён в базе

    // При желании можно добавить confirmPassword и валидировать совпадение
}
