package com.nationalbank.banking.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetCategoryRequest {
    private Long id;
    private Long budgetId;
    private String category;
    private BigDecimal amount;
}
