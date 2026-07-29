package com.nationalbank.banking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;
}
