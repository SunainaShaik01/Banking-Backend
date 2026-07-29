package com.nationalbank.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {

    private Long id;

    @NotNull
    private LocalDate expenseDate;

    @NotBlank
    private String category;

    @NotNull
    private BigDecimal amount;
}
