package com.nationalbank.banking.service;

import com.nationalbank.banking.dto.*;
import com.nationalbank.banking.entity.Expense;
import com.nationalbank.banking.entity.User;
import com.nationalbank.banking.exception.BadRequestException;
import com.nationalbank.banking.repository.ExpenseRepository;
import com.nationalbank.banking.repository.UserRepository;
import com.nationalbank.banking.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    public List<ExpenseResponse> getExpensesForDate(Long userId, LocalDate date) {
        authorizationService.assertCanAccessUser(userId);
        return expenseRepository.findByUserIdAndExpenseDate(userId, date).stream()
                .map(expense -> new ExpenseResponse(expense.getId(), expense.getCategory(), expense.getAmount()))
                .toList();
    }

    @Transactional
    public void saveExpenses(Long userId, List<ExpenseRequest> requests) {
        authorizationService.assertCanAccessUser(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        for (ExpenseRequest request : requests) {
            Expense expense;
            if (request.getId() != null) {
                expense = expenseRepository.findById(request.getId())
                        .orElseThrow(() -> new BadRequestException("Expense not found"));
                if (!expense.getUser().getId().equals(userId)) {
                    throw new BadRequestException("Expense does not belong to user");
                }
            } else {
                expense = expenseRepository
                        .findByUserIdAndExpenseDateAndCategory(userId, request.getExpenseDate(), request.getCategory())
                        .orElseGet(Expense::new);
                expense.setUser(user);
                expense.setExpenseDate(request.getExpenseDate());
                expense.setCategory(request.getCategory());
            }
            expense.setAmount(request.getAmount());
            expenseRepository.save(expense);
        }
    }

    public ExpenseDashboardResponse getDashboard(Long userId, int month) {
        authorizationService.assertCanAccessUser(userId);
        List<Expense> expenses = expenseRepository.findByUserIdAndMonth(userId, month);

        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        Map<String, BigDecimal> breakdown = new LinkedHashMap<>();

        for (Expense expense : expenses) {
            breakdown.merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
            if ("Additional Income".equals(expense.getCategory())) {
                totalIncome = totalIncome.add(expense.getAmount());
            } else {
                totalExpenses = totalExpenses.add(expense.getAmount());
            }
        }

        return new ExpenseDashboardResponse(totalExpenses, totalIncome, breakdown);
    }

    public List<ExpenseTrendResponse> getTrends(Long userId) {
        authorizationService.assertCanAccessUser(userId);
        List<Expense> expenses = expenseRepository.findAllByUserIdOrderByExpenseDate(userId);

        Map<Integer, Map<String, BigDecimal>> monthlyData = new TreeMap<>();
        for (Expense expense : expenses) {
            int month = expense.getExpenseDate().getMonthValue();
            monthlyData
                    .computeIfAbsent(month, key -> new LinkedHashMap<>())
                    .merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
        }

        return monthlyData.entrySet().stream()
                .map(entry -> new ExpenseTrendResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
