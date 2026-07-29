package com.nationalbank.banking.service;

import com.nationalbank.banking.dto.*;
import com.nationalbank.banking.entity.*;
import com.nationalbank.banking.exception.BadRequestException;
import com.nationalbank.banking.repository.BudgetRepository;
import com.nationalbank.banking.repository.UserRepository;
import com.nationalbank.banking.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public BudgetResponse createBudget(BudgetCreateRequest request) {
        authorizationService.assertCanAccessUser(request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Budget budget = budgetRepository.findByUserIdAndMonthAndYear(
                request.getUserId(), request.getMonth(), request.getYear()
        ).orElseGet(Budget::new);

        budget.setUser(user);
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponse saveIncomeSources(Long budgetId, List<BudgetIncomeRequest> requests) {
        Budget budget = getBudgetForUpdate(budgetId);
        budget.getIncomeSources().clear();

        for (BudgetIncomeRequest request : requests) {
            BudgetIncomeSource income = new BudgetIncomeSource();
            income.setBudget(budget);
            income.setSourceName(request.getSourceName());
            income.setAmount(request.getAmount());
            budget.getIncomeSources().add(income);
        }

        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponse saveFixedExpenses(Long budgetId, List<BudgetCategoryRequest> requests) {
        Budget budget = getBudgetForUpdate(budgetId);
        budget.getFixedExpenses().clear();

        for (BudgetCategoryRequest request : requests) {
            BudgetFixedExpense expense = new BudgetFixedExpense();
            expense.setBudget(budget);
            expense.setCategory(request.getCategory());
            expense.setAmount(request.getAmount());
            budget.getFixedExpenses().add(expense);
        }

        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponse saveVariableExpenses(Long budgetId, List<BudgetCategoryRequest> requests) {
        Budget budget = getBudgetForUpdate(budgetId);
        budget.getVariableExpenses().clear();

        for (BudgetCategoryRequest request : requests) {
            BudgetVariableExpense expense = new BudgetVariableExpense();
            expense.setBudget(budget);
            expense.setCategory(request.getCategory());
            expense.setAmount(request.getAmount());
            budget.getVariableExpenses().add(expense);
        }

        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponse saveSavingGoals(Long budgetId, List<BudgetSavingGoalRequest> requests) {
        Budget budget = getBudgetForUpdate(budgetId);
        budget.getSavingGoals().clear();

        for (BudgetSavingGoalRequest request : requests) {
            BudgetSavingGoal goal = new BudgetSavingGoal();
            goal.setBudget(budget);
            goal.setName(request.getName());
            goal.setAmount(request.getAmount());
            budget.getSavingGoals().add(goal);
        }

        return toResponse(budgetRepository.save(budget));
    }

    public BudgetResponse getBudgetForMonth(Long userId, Integer month, Integer year) {
        authorizationService.assertCanAccessUser(userId);
        Budget budget = budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new BadRequestException("Budget not found"));
        return toResponse(budget);
    }

    private Budget getBudgetForUpdate(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BadRequestException("Budget not found"));
        authorizationService.assertCanAccessUser(budget.getUser().getId());
        return budget;
    }

    private BudgetResponse toResponse(Budget budget) {
        List<BudgetIncomeRequest> incomeSources = budget.getIncomeSources().stream()
                .map(item -> {
                    BudgetIncomeRequest dto = new BudgetIncomeRequest();
                    dto.setId(item.getId());
                    dto.setBudgetId(budget.getId());
                    dto.setSourceName(item.getSourceName());
                    dto.setAmount(item.getAmount());
                    return dto;
                }).toList();

        List<BudgetCategoryRequest> fixedExpenses = budget.getFixedExpenses().stream()
                .map(item -> {
                    BudgetCategoryRequest dto = new BudgetCategoryRequest();
                    dto.setId(item.getId());
                    dto.setBudgetId(budget.getId());
                    dto.setCategory(item.getCategory());
                    dto.setAmount(item.getAmount());
                    return dto;
                }).toList();

        List<BudgetCategoryRequest> variableExpenses = budget.getVariableExpenses().stream()
                .map(item -> {
                    BudgetCategoryRequest dto = new BudgetCategoryRequest();
                    dto.setId(item.getId());
                    dto.setBudgetId(budget.getId());
                    dto.setCategory(item.getCategory());
                    dto.setAmount(item.getAmount());
                    return dto;
                }).toList();

        List<BudgetSavingGoalRequest> savingGoals = budget.getSavingGoals().stream()
                .map(item -> {
                    BudgetSavingGoalRequest dto = new BudgetSavingGoalRequest();
                    dto.setId(item.getId());
                    dto.setBudgetId(budget.getId());
                    dto.setName(item.getName());
                    dto.setAmount(item.getAmount());
                    return dto;
                }).toList();

        return new BudgetResponse(
                budget.getId(),
                budget.getUser().getId(),
                budget.getMonth(),
                budget.getYear(),
                incomeSources,
                fixedExpenses,
                variableExpenses,
                savingGoals
        );
    }
}
