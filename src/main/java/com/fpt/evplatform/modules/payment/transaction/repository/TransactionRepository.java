package com.fpt.evplatform.modules.payment.transaction.repository;

import com.fpt.evplatform.modules.payment.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'PAYMENT' AND t.status = 'SUCCEEDED'")
    Long totalPayment();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'REFUND' AND t.status = 'REFUNDED'")
    Long totalRefund();

    List<Transaction> findByTypeAndStatusOrderByCreatedAtDesc(String type, String status);

}