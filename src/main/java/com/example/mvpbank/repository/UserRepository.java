package com.example.mvpbank.repository;

import com.example.mvpbank.model.User; // Импортируем нашу сущность User
import org.springframework.data.jpa.repository.JpaRepository; // Интерфейс JPA-репозитория
import org.springframework.stereotype.Repository; // Аннотация для обозначения класса как репозитория

import java.util.Optional; // Используется для безопасного поиска

@Repository // Сообщает Spring, что это компонент-репозиторий
public interface UserRepository extends JpaRepository<User, Long> {

    // Метод для поиска пользователя по имени пользователя
    Optional<User> findByUsername(String username);

    // Метод для поиска пользователя по email (может быть полезен при регистрации/восстановлении пароля)
    Optional<User> findByEmail(String email);

    // Метод проверки существования пользователя по имени пользователя
    boolean existsByUsername(String username);

    // Метод проверки существования пользователя по email
    boolean existsByEmail(String email);
}
