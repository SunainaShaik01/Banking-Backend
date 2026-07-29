package com.nationalbank.banking.config;

import com.nationalbank.banking.entity.*;
import com.nationalbank.banking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ExpenseRepository expenseRepository;
    private final DebtRepository debtRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(createRole("ROLE_USER")));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(createRole("ROLE_ADMIN")));

        User admin = userRepository.findByEmail("admin@bank.com").orElseGet(() -> {
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setEmail("admin@bank.com");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRoles(Set.of(adminRole));
            return userRepository.save(user);
        });

        User demoUser = userRepository.findByEmail("user@bank.com").orElseGet(() -> {
            User user = new User();
            user.setFirstName("Demo");
            user.setLastName("User");
            user.setEmail("user@bank.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(Set.of(userRole));
            return userRepository.save(user);
        });

        if (transactionRepository.findByUserIdOrderByTimestampDesc(demoUser.getId()).isEmpty()) {
            createTransaction(demoUser, TransactionType.deposit, new BigDecimal("2500.00"));
            createTransaction(demoUser, TransactionType.withdraw, new BigDecimal("250.00"));
        }

        if (expenseRepository.findByUserIdAndExpenseDate(demoUser.getId(), LocalDate.now()).isEmpty()) {
            createExpense(demoUser, LocalDate.now(), "Rent", new BigDecimal("1200.00"));
            createExpense(demoUser, LocalDate.now(), "Groceries", new BigDecimal("350.00"));
            createExpense(demoUser, LocalDate.now().minusMonths(1), "Transportation", new BigDecimal("120.00"));
        }

        if (debtRepository.findByUserId(demoUser.getId()).isEmpty()) {
            Debt debt = new Debt();
            debt.setUser(demoUser);
            debt.setType("Credit Card");
            debt.setAmount(new BigDecimal("5000.00"));
            debt.setInterestRate(new BigDecimal("18.50"));
            debt.setMinPayment(new BigDecimal("150.00"));
            debt.setAdditionalPayment(new BigDecimal("50.00"));
            debtRepository.save(debt);
        }

        if (admin.getId() != null && transactionRepository.findByUserIdOrderByTimestampDesc(admin.getId()).isEmpty()) {
            createTransaction(admin, TransactionType.deposit, new BigDecimal("10000.00"));
        }
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    private void createTransaction(User user, TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(type);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    private void createExpense(User user, LocalDate date, String category, BigDecimal amount) {
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setExpenseDate(date);
        expense.setCategory(category);
        expense.setAmount(amount);
        expenseRepository.save(expense);
    }
}
