package com.example.mvpbank.service;

import com.example.mvpbank.dto.TransferRequest;
import com.example.mvpbank.model.Account;
import com.example.mvpbank.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository; // Репозиторий для доступа к счетам

    @Transactional // Весь метод выполняется в одной транзакции
    public void transfer(TransferRequest request) {
        // Находим счёт-отправитель
        Account from = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        // Находим счёт-получатель
        Account to = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient account not found"));

        BigDecimal amount = request.getAmount();

        // Проверяем, что у отправителя хватает средств
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Уменьшаем баланс отправителя
        from.setBalance(from.getBalance().subtract(amount));

        // Увеличиваем баланс получателя
        to.setBalance(to.getBalance().add(amount));

        // Сохраняем изменения
        accountRepository.save(from);
        accountRepository.save(to);
    }
}
