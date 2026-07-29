package com.nationalbank.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class BudgetResponse {
    private Long id;
    private Long userId;
    private Integer month;
    private Integer year;
    private List<BudgetIncomeRequest> incomeSources;
    private List<BudgetCategoryRequest> fixedExpenses;
    private List<BudgetCategoryRequest> variableExpenses;
    private List<BudgetSavingGoalRequest> savingGoals;
}
