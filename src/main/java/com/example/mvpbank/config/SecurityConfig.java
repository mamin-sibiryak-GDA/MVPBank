// Конфигурация безопасности для Spring Security

package com.example.mvpbank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Говорим Spring, что это класс конфигурации
@EnableWebSecurity // Включаем поддержку Spring Security
@RequiredArgsConstructor // Lombok создаёт конструктор для всех final-полей
public class SecurityConfig {

    @Bean // Определяем бин SecurityFilterChain, который конфигурирует цепочку фильтров безопасности
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF, так как у нас REST API (нет форм)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Только ADMIN может использовать эти эндпоинты
                        .requestMatchers("/api/auth/**").permitAll() // Разрешаем доступ ко всем auth-эндпоинтам
                        .requestMatchers("/api/accounts/**").authenticated()
                        .anyRequest().authenticated() // Все остальные требуют авторизацию
                )
                .httpBasic(httpBasic -> {});

        return http.build(); // Собираем и возвращаем конфигурацию
    }

    @Bean // Определяем бин для PasswordEncoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt — безопасный способ хеширования паролей
    }

    @Bean // Определяем бин AuthenticationManager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Получаем AuthenticationManager из конфигурации
    }
}
