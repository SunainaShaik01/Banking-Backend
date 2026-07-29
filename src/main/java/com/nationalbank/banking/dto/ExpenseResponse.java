package com.nationalbank.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private String category;
    private BigDecimal amount;
}
