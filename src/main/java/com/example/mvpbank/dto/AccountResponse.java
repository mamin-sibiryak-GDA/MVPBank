// DTO для отправки информации о счёте без рекурсии
package com.example.mvpbank.dto;

import java.math.BigDecimal;

// Используем record для лаконичного и безопасного DTO
public record AccountResponse(
        Long id,              // ID счёта
        BigDecimal balance    // Баланс счёта
) {
}

