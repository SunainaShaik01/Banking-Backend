package com.nationalbank.banking.service;

import com.nationalbank.banking.dto.TransactionRequest;
import com.nationalbank.banking.dto.TransactionResponse;
import com.nationalbank.banking.entity.Transaction;
import com.nationalbank.banking.entity.TransactionType;
import com.nationalbank.banking.entity.User;
import com.nationalbank.banking.exception.BadRequestException;
import com.nationalbank.banking.repository.TransactionRepository;
import com.nationalbank.banking.repository.UserRepository;
import com.nationalbank.banking.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public TransactionResponse deposit(TransactionRequest request) {
        authorizationService.assertCanAccessUser(request.getUserId());
        return createTransaction(request, TransactionType.deposit);
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest request) {
        authorizationService.assertCanAccessUser(request.getUserId());

        BigDecimal balance = getBalance(request.getUserId());
        if (balance.compareTo(request.getAmount()) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        return createTransaction(request, TransactionType.withdraw);
    }

    public BigDecimal getBalance(Long userId) {
        authorizationService.assertCanAccessUser(userId);
        return transactionRepository.calculateBalance(userId);
    }

    public List<TransactionResponse> getHistory(Long userId) {
        authorizationService.assertCanAccessUser(userId);
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse createTransaction(TransactionRequest request, TransactionType type) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(type);
        transaction.setAmount(request.getAmount());

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTimestamp()
        );
    }
}
