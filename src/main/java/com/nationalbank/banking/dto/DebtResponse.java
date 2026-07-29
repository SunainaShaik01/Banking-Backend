package com.nationalbank.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DebtResponse {
    private Long id;
    private String type;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private BigDecimal minPayment;
    private BigDecimal additionalPayment;
}
