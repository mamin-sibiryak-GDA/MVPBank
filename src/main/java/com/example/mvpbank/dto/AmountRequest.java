package com.example.mvpbank.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountRequest {
    private BigDecimal amount;
}
