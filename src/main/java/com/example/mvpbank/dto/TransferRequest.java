package com.example.mvpbank.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private Long fromAccountId;     // ID счёта, с которого переводят деньги
    private Long toAccountId;       // ID счёта, на который переводят деньги
    private BigDecimal amount;      // Сумма перевода
}

