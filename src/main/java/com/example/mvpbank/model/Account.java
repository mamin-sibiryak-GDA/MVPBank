// Сущность банковского счёта

package com.example.mvpbank.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Указывает, что класс — JPA-сущность
@Table(name = "accounts") // Таблица в базе данных
@Getter // Lombok: геттеры
@Setter // Lombok: сеттеры
@NoArgsConstructor // Lombok: конструктор без аргументов
@AllArgsConstructor // Lombok: конструктор со всеми аргументами
@Builder // Lombok: паттерн Builder
public class Account {

    @Id // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент
    private Long id;

    @ManyToOne // Много счетов может принадлежать одному пользователю
    @JoinColumn(name = "user_id", nullable = false) // Внешний ключ на таблицу users
    private User user;

    @Column(nullable = false, unique = true) // Номер счёта должен быть уникальным
    private String accountNumber;

    @Column(nullable = false) // Баланс не может быть null
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist // Метод вызывается перед сохранением сущности в базу данных
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // Устанавливаем дату создания вручную
    }

}
