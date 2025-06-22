// Репозиторий для работы с банковскими счетами

package com.example.mvpbank.repository;

import com.example.mvpbank.model.Account;
import com.example.mvpbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Указывает, что интерфейс является компонентом Spring и работает с базой данных
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Метод для получения всех счетов пользователя
    List<Account> findByUser(User user);

    // Метод для поиска счёта по его номеру (например, для перевода или проверки)
    Account findByAccountNumber(String accountNumber);

}
