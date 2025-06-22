package com.example.mvpbank.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор пользователя

    @Column(unique = true, nullable = false)
    private String username; // Имя пользователя (логин)

    @Column(nullable = false)
    private String password; // Хешированный пароль

    @Column(nullable = false, unique = true)
    private String email; // Email пользователя

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // Роль пользователя (например, ROLE_USER, ROLE_ADMIN)

    // Возвращаем авторитеты (права доступа) на основе роли пользователя
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name()) // Преобразуем enum в строку с префиксом "ROLE_"
        );
    }

    @CreationTimestamp // Hibernate автоматически проставит дату при создании записи
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "user",                // Название поля в Account, которое связано с User
            cascade = CascadeType.REMOVE,     // При удалении пользователя — удалить все его счета
            orphanRemoval = true,             // Также удалить счёт, если он был удалён из списка
            fetch = FetchType.LAZY            // Счета подгружаются только при необходимости
    )
    private List<Account> accounts;       // Список счетов, принадлежащих пользователю

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
