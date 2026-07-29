package com.nationalbank.banking.dto;

import com.nationalbank.banking.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransactionResponse {
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
