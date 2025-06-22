// Класс CustomUserDetails реализует интерфейс UserDetails от Spring Security
// Он необходим, чтобы Spring Security знал, как извлекать данные пользователя из нашей сущности User

package com.example.mvpbank.security;

import com.example.mvpbank.model.User; // Импорт модели пользователя
import org.springframework.security.core.GrantedAuthority; // Интерфейс для представления прав пользователя
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Упрощенная реализация GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails; // Интерфейс, который должен реализовывать пользователь

import java.util.Collection;
import java.util.Collections; // Используется, если у пользователя пока одна роль или нет ролей

public class CustomUserDetails implements UserDetails {

    private final User user; // Объект нашего пользователя

    // Конструктор принимает пользователя и сохраняет его для использования в методах интерфейса
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Возвращает права пользователя. Пока одна роль USER по умолчанию.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    // Возвращает пароль пользователя
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Возвращает имя пользователя
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Аккаунт не просрочен (можно сделать более сложную логику позже)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Аккаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Учетные данные не просрочены
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Аккаунт активен
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Метод, позволяющий получить оригинальный объект User при необходимости
    public User getUser() {
        return user;
    }
}
