package com.nationalbank.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExpenseTrendResponse {
    private int month;
    private Map<String, BigDecimal> categorySpending;
}
