package com.nationalbank.banking.controller;

import com.nationalbank.banking.dto.DebtRequest;
import com.nationalbank.banking.dto.DebtResponse;
import com.nationalbank.banking.service.DebtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;

    @PostMapping("/save")
    public DebtResponse saveDebt(@Valid @RequestBody DebtRequest request) {
        return debtService.saveDebt(request);
    }

    @GetMapping("/user/{userId}")
    public List<DebtResponse> getDebtsByUserId(@PathVariable Long userId) {
        return debtService.getDebtsByUserId(userId);
    }
}
