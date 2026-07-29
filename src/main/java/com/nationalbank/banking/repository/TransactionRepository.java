package com.nationalbank.banking.repository;

import com.nationalbank.banking.entity.Transaction;
import com.nationalbank.banking.entity.TransactionType;
import com.nationalbank.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByTimestampDesc(Long userId);

    @Query("""
            SELECT COALESCE(SUM(CASE WHEN t.type = com.nationalbank.banking.entity.TransactionType.deposit THEN t.amount ELSE -t.amount END), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
            """)
    BigDecimal calculateBalance(@Param("userId") Long userId);

    List<Transaction> findByUser(User user);
}
