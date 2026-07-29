package com.nationalbank.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DebtRequest {

    @NotBlank
    private String type;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interestRate;

    @NotNull
    private BigDecimal minPayment;

    private BigDecimal additionalPayment;

    @NotNull
    private Long userId;
}
