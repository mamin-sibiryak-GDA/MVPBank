// Сервис для работы с банковскими счетами

package com.example.mvpbank.service;

import com.example.mvpbank.model.Account;
import com.example.mvpbank.model.User;
import com.example.mvpbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service // Указывает, что класс является сервисом Spring
@RequiredArgsConstructor // Lombok: генерирует конструктор с final полями
public class AccountService {

    private final AccountRepository accountRepository; // Репозиторий для доступа к счетам

    // Метод создания нового счёта для пользователя
    public Account createAccount(User user) {
        // Генерация уникального номера счёта
        String accountNumber = UUID.randomUUID().toString();

        // Создаём новый счёт с нулевым балансом
        Account account = Account.builder()
                .user(user)
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .build();

        // Сохраняем счёт в базе данных и возвращаем его
        return accountRepository.save(account);
    }

    // Получить список всех счетов пользователя
    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findByUser(user);
    }

    // Получить счёт по его номеру
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}
