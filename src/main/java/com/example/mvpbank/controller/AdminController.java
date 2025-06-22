package com.example.mvpbank.controller;

import com.example.mvpbank.dto.AccountResponse;
import com.example.mvpbank.dto.AmountRequest;
import com.example.mvpbank.dto.UserResponse;
import com.example.mvpbank.model.Account;
import com.example.mvpbank.model.User;
import com.example.mvpbank.repository.AccountRepository;
import com.example.mvpbank.repository.UserRepository;
import com.example.mvpbank.service.AccountService;
import com.example.mvpbank.service.KafkaSenderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final KafkaSenderService kafkaSenderService;      // Сервис для отправки событий в Kafka

    // Получить список всех пользователей
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = users.stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // Получить счета определённого пользователя
    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable Long userId) {

        // Получаем объект User по id
        User user = userRepository.findById(userId)
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

    // Удалить пользователя
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) return ResponseEntity.notFound().build();

        userRepository.deleteById(userId);
        kafkaSenderService.sendMessage("account-logs", "Admin deleted user with ID: " + userId); // лог в Kafka

        return ResponseEntity.ok("User deleted");
    }

    // Удалить счёт
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        if (!accountRepository.existsById(accountId)) return ResponseEntity.notFound().build();

        accountRepository.deleteById(accountId);
        kafkaSenderService.sendMessage("account-logs", "Admin deleted account with ID: " + accountId); // лог в Kafka

        return ResponseEntity.ok("Account deleted");
    }

    // Пополнить счёт
    @PostMapping("/accounts/{accountId}/deposit")
    @Transactional
    public ResponseEntity<String> depositToAccount(@PathVariable Long accountId, @RequestBody AmountRequest amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance().add(amount.getAmount()));

        accountRepository.save(account);
        kafkaSenderService.sendMessage("account-logs", "Deposit to account " + accountId + ": " + amount); // лог в Kafka

        return ResponseEntity.ok("Deposit successful");
    }

    // Снять со счёта
    @PostMapping("/accounts/{accountId}/withdraw")
    @Transactional
    public ResponseEntity<String> withdrawFromAccount(@PathVariable Long accountId, @RequestBody AmountRequest amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setBalance(account.getBalance().subtract(amount.getAmount()));

        accountRepository.save(account);
        kafkaSenderService.sendMessage("account-logs", "Withdraw from account " + accountId + ": " + amount); // лог в Kafka

        return ResponseEntity.ok("Withdraw successful");
    }
}
