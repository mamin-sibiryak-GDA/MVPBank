// DTO для запроса регистрации пользователя

package com.example.mvpbank.dto;

import com.example.mvpbank.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor // Lombok: создаёт пустой конструктор (нужен для Jackson)
@AllArgsConstructor // Lombok: создаёт конструктор со всеми полями
public class UserRegistrationRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username; // Имя пользователя (логин)

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password; // Пароль

    @Email(message = "Неправильный формат Email")
    private String email;    // Email (используется для связи и уникальности)

    @NotBlank(message = "Должна быть роль")
    private Role role;     // Роль (например, ROLE_USER, ROLE_ADMIN)

}
