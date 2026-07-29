package com.nationalbank.banking.controller;

import com.nationalbank.banking.dto.*;
import com.nationalbank.banking.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public BudgetResponse createBudget(@Valid @RequestBody BudgetCreateRequest request) {
        return budgetService.createBudget(request);
    }

    @PostMapping("/{budgetId}/income")
    public BudgetResponse saveIncomeSources(
            @PathVariable Long budgetId,
            @RequestBody List<BudgetIncomeRequest> requests
    ) {
        return budgetService.saveIncomeSources(budgetId, requests);
    }

    @PostMapping("/{budgetId}/fixedExpense")
    public BudgetResponse saveFixedExpenses(
            @PathVariable Long budgetId,
            @RequestBody List<BudgetCategoryRequest> requests
    ) {
        return budgetService.saveFixedExpenses(budgetId, requests);
    }

    @PostMapping("/{budgetId}/variableExpense")
    public BudgetResponse saveVariableExpenses(
            @PathVariable Long budgetId,
            @RequestBody List<BudgetCategoryRequest> requests
    ) {
        return budgetService.saveVariableExpenses(budgetId, requests);
    }

    @PostMapping("/{budgetId}/savingGoals")
    public BudgetResponse saveSavingGoals(
            @PathVariable Long budgetId,
            @RequestBody List<BudgetSavingGoalRequest> requests
    ) {
        return budgetService.saveSavingGoals(budgetId, requests);
    }

    @GetMapping("/{userId}/{month}/{year}")
    public BudgetResponse getBudgetForMonth(
            @PathVariable Long userId,
            @PathVariable Integer month,
            @PathVariable Integer year
    ) {
        return budgetService.getBudgetForMonth(userId, month, year);
    }
}
