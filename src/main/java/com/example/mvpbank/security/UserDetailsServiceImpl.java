package com.example.mvpbank.security;

import com.example.mvpbank.model.User; // Импорт нашей модели пользователя
import com.example.mvpbank.repository.UserRepository; // Импорт репозитория для работы с БД
import org.springframework.beans.factory.annotation.Autowired; // Аннотация для автоматической зависимости
import org.springframework.security.core.userdetails.UserDetails; // Интерфейс, описывающий пользователя
import org.springframework.security.core.userdetails.UserDetailsService; // Интерфейс, который мы реализуем
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Исключение, если пользователь не найден
import org.springframework.stereotype.Service; // Обозначает, что этот класс — сервис Spring

@Service // Сообщает Spring, что этот класс нужно создать как компонент и управлять им
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired // Автоматически внедряет зависимость через конструктор
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Метод, который вызывается Spring Security для загрузки пользователя по имени
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Пытаемся найти пользователя по имени, если не нашли — бросаем исключение
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Возвращаем объект, реализующий UserDetails (CustomUserDetails)
        return new CustomUserDetails(user);
    }
}
