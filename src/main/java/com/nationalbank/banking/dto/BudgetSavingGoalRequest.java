package com.nationalbank.banking.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetSavingGoalRequest {
    private Long id;
    private Long budgetId;
    private String name;
    private BigDecimal amount;
}
