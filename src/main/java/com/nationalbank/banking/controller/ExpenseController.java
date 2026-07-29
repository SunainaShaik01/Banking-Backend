package com.nationalbank.banking.controller;

import com.nationalbank.banking.dto.ExpenseDashboardResponse;
import com.nationalbank.banking.dto.ExpenseRequest;
import com.nationalbank.banking.dto.ExpenseResponse;
import com.nationalbank.banking.dto.ExpenseTrendResponse;
import com.nationalbank.banking.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/{userId}/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public List<ExpenseResponse> getExpensesForDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return expenseService.getExpensesForDate(userId, date);
    }

    @PostMapping
    public void saveExpenses(
            @PathVariable Long userId,
            @Valid @RequestBody List<ExpenseRequest> requests
    ) {
        expenseService.saveExpenses(userId, requests);
    }

    @GetMapping("/dashboard")
    public ExpenseDashboardResponse getDashboard(
            @PathVariable Long userId,
            @RequestParam int month
    ) {
        return expenseService.getDashboard(userId, month);
    }

    @GetMapping("/trends")
    public List<ExpenseTrendResponse> getTrends(@PathVariable Long userId) {
        return expenseService.getTrends(userId);
    }
}
