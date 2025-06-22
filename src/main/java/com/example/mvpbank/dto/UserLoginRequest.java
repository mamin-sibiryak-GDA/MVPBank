package com.example.mvpbank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
}
