// Конфигурация безопасности Spring Security с использованием JWT
package com.example.mvpbank.config;

import com.example.mvpbank.security.JwtAuthFilter;
import com.example.mvpbank.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Помечает класс как конфигурационный бин Spring
@EnableWebSecurity // Включает поддержку безопасности веб-приложений
@EnableMethodSecurity // Разрешает использование @PreAuthorize, @Secured и т.п.
@RequiredArgsConstructor // Автоматически создаёт конструктор с финальными полями
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // JWT-фильтр для проверки токенов
    private final UserDetailsServiceImpl userDetailsService; // Кастомный сервис загрузки пользователей

    @Bean // Бин для хэширования паролей
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Бин менеджера аутентификации, нужного для login
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean // Основной фильтр цепочки безопасности
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF (актуально для REST API)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Без сохранения сессий
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Разрешаем доступ к /api/auth/** без авторизации
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Добавляем наш JWT-фильтр до UsernamePasswordAuthenticationFilter

        return http.build();
    }
}
