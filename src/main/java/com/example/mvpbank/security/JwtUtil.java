// Утилита для генерации и валидации JWT токенов
package com.example.mvpbank.security;

import com.example.mvpbank.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Секретный ключ, закодированный в base64 (должен быть не короче 256 бит)
    private static final String SECRET_KEY = "708de2acd93b25537f31708acb21623317030d37df28e305c5e4a786bdb0bb9b";

    // Ключ, с которым подписываются и проверяются токены
    private final SecretKey key;

    // Конструктор: декодирует строку и создаёт ключ для подписи
    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    // Генерация JWT токена по имени пользователя и роли
    public String generateToken(String username, Role role) {
        long nowMillis = System.currentTimeMillis(); // Текущее время в миллисекундах
        long expMillis = nowMillis + 1000 * 60 * 60; // Время истечения — через 1 час

        return Jwts.builder()
                .subject(username)                      // Добавляем subject — имя пользователя
                .claim("role", role)                    // Добавляем кастомный claim с ролью
                .issuedAt(new Date(nowMillis))          // Указываем время выпуска токена
                .expiration(new Date(expMillis))        // Указываем срок действия токена
                .signWith(key)                          // Подписываем токен ключом (алгоритм автоматически HMAC-SHA256)
                .compact();                             // Строим строковое представление токена
    }

    // Вспомогательный метод: извлекает все claims (данные) из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                            // Создаём парсер
                .verifyWith(key)                        // Указываем ключ для верификации подписи
                .build()                                // Строим парсер
                .parseSignedClaims(token)               // Разбираем токен и проверяем подпись
                .getPayload();                          // Получаем содержимое (claims)
    }

    // Извлечение имени пользователя (subject) из токена
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Извлечение роли из токена (поле "role")
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // Проверка, действителен ли токен (не истёк ли)
    public boolean isTokenValid(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.after(new Date());
    }
}
