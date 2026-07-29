package com.nationalbank.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExpenseDashboardResponse {
    private BigDecimal totalExpenses;
    private BigDecimal totalIncome;
    private Map<String, BigDecimal> categoryBreakdown;
}
