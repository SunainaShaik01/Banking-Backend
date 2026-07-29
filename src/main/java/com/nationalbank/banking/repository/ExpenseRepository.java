package com.nationalbank.banking.repository;

import com.nationalbank.banking.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdAndExpenseDate(Long userId, LocalDate expenseDate);

    Optional<Expense> findByUserIdAndExpenseDateAndCategory(Long userId, LocalDate expenseDate, String category);

    @Query("""
            SELECT e FROM Expense e
            WHERE e.user.id = :userId
              AND EXTRACT(MONTH FROM e.expenseDate) = :month
            """)
    List<Expense> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") int month);

    @Query("""
            SELECT e FROM Expense e
            WHERE e.user.id = :userId
            ORDER BY e.expenseDate
            """)
    List<Expense> findAllByUserIdOrderByExpenseDate(@Param("userId") Long userId);
}
