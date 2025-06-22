// Контроллер для работы с банковскими счетами
package com.example.mvpbank.controller;

import com.example.mvpbank.dto.AccountResponse;
import com.example.mvpbank.model.Account;
import com.example.mvpbank.model.User;
import com.example.mvpbank.repository.UserRepository;
import com.example.mvpbank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Указывает, что класс — REST-контроллер
@RequestMapping("/api/accounts") // Базовый путь для всех эндпоинтов
@RequiredArgsConstructor // Lombok-аннотация для создания конструктора с final полями
public class AccountController {

    private final UserRepository userRepository; // Для получения сущности User по username
    private final AccountService accountService; // Сервис работы со счетами

    // POST-запрос: создать новый счёт для текущего пользователя
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@AuthenticationPrincipal UserDetails userDetails) {

        // Получаем объект User по username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создаём новый счёт
        Account createdAccount = accountService.createAccount(user);

        // Возвращаем только нужные данные через DTO
        AccountResponse response = new AccountResponse(createdAccount.getId(), createdAccount.getBalance());

        // Возвращаем DTO клиенту
        return ResponseEntity.ok(response);
    }

    // GET-запрос: получить список счетов текущего пользователя
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(@AuthenticationPrincipal UserDetails userDetails) {

        // Получаем объект User по username
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Получаем список счетов пользователя
        List<Account> accounts = accountService.getAccountsByUser(user);

        // Преобразуем счета в DTO-объекты, чтобы избежать циклических зависимостей в JSON
        List<AccountResponse> response = accounts.stream()
                .map(account -> new AccountResponse(account.getId(), account.getBalance()))
                .toList();

        // Возвращаем список DTO с HTTP 200 OK
        return ResponseEntity.ok(response);
    }
}
