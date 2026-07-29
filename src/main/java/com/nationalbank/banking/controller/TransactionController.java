package com.nationalbank.banking.controller;

import com.nationalbank.banking.dto.TransactionRequest;
import com.nationalbank.banking.dto.TransactionResponse;
import com.nationalbank.banking.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionResponse deposit(@Valid @RequestBody TransactionRequest request) {
        return transactionService.deposit(request);
    }

    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@Valid @RequestBody TransactionRequest request) {
        return transactionService.withdraw(request);
    }

    @GetMapping("/balance/{userId}")
    public BigDecimal getBalance(@PathVariable Long userId) {
        return transactionService.getBalance(userId);
    }

    @GetMapping("/history/{userId}")
    public List<TransactionResponse> getHistory(@PathVariable Long userId) {
        return transactionService.getHistory(userId);
    }
}
