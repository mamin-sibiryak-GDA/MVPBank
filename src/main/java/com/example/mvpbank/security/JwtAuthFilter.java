// Фильтр для проверки JWT в каждом запросе
package com.example.mvpbank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Компонент Spring — автоматически регистрируется как бин
@RequiredArgsConstructor // Автоматически создаёт конструктор с final полями
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // Утилита для работы с JWT
    private final UserDetailsServiceImpl userDetailsService; // Сервис для загрузки пользователей

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // Получаем заголовок Authorization
        final String jwt;
        final String username;

        // Проверяем, что заголовок начинается с "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Если нет токена — продолжаем без аутентификации
            return;
        }

        jwt = authHeader.substring(7); // Убираем "Bearer" и оставляем только токен
        username = jwtUtil.extractUsername(jwt); // Извлекаем имя пользователя из токена

        // Проверяем, что пользователь ещё не аутентифицирован
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Загружаем пользователя из БД

            if (jwtUtil.isTokenValid(jwt)) { // Проверяем, что токен не истёк и валиден

                // Создаём объект аутентификации и заполняем контекст безопасности
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Устанавливаем аутентификацию в контекст
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Продолжаем выполнение цепочки фильтров
        filterChain.doFilter(request, response);
    }
}
