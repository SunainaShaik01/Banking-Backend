package com.nationalbank.banking.service;

import com.nationalbank.banking.dto.DebtRequest;
import com.nationalbank.banking.dto.DebtResponse;
import com.nationalbank.banking.entity.Debt;
import com.nationalbank.banking.entity.User;
import com.nationalbank.banking.exception.BadRequestException;
import com.nationalbank.banking.repository.DebtRepository;
import com.nationalbank.banking.repository.UserRepository;
import com.nationalbank.banking.security.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public DebtResponse saveDebt(DebtRequest request) {
        authorizationService.assertCanAccessUser(request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Debt debt = new Debt();
        debt.setUser(user);
        debt.setType(request.getType());
        debt.setAmount(request.getAmount());
        debt.setInterestRate(request.getInterestRate());
        debt.setMinPayment(request.getMinPayment());
        debt.setAdditionalPayment(
                request.getAdditionalPayment() != null ? request.getAdditionalPayment() : BigDecimal.ZERO
        );

        return toResponse(debtRepository.save(debt));
    }

    public List<DebtResponse> getDebtsByUserId(Long userId) {
        authorizationService.assertCanAccessUser(userId);
        return debtRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private DebtResponse toResponse(Debt debt) {
        return new DebtResponse(
                debt.getId(),
                debt.getType(),
                debt.getAmount(),
                debt.getInterestRate(),
                debt.getMinPayment(),
                debt.getAdditionalPayment()
        );
    }
}
