package com.fpt.evplatform.modules.payment.transaction.service;

import com.fpt.evplatform.modules.payment.transaction.dto.response.RevenueResponse;
import com.fpt.evplatform.modules.payment.transaction.entity.Transaction;
import com.fpt.evplatform.modules.payment.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;

    // Ghi nhận payment
    public void recordPayment(Integer userId, Integer referenceId, String referenceType, Long amount) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setReferenceId(referenceId);
        tx.setReferenceType(referenceType);
        tx.setAmount(amount);
        tx.setType("PAYMENT");
        tx.setStatus("SUCCEEDED");
        repo.save(tx);
    }

    public void recordRefund(Integer userId, Integer referenceId, String referenceType, Long amount) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setReferenceId(referenceId);
        tx.setReferenceType(referenceType);
        tx.setAmount(amount);
        tx.setType("REFUND");
        tx.setStatus("PENDING");
        repo.save(tx);
    }

    public List<Transaction> getPendingRefunds() {
        return repo.findByTypeAndStatusOrderByCreatedAtDesc("REFUND", "PENDING");
    }

    // Tính doanh thu
    public RevenueResponse getRevenue() {
        Long payment = Optional.ofNullable(repo.totalPayment()).orElse(0L);
        Long refund = Optional.ofNullable(repo.totalRefund()).orElse(0L);
        Long net = payment - refund;

        return new RevenueResponse(payment, refund, net);
    }

    public void markRefunded(Integer transactionId) {
        Transaction tx = repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!"REFUND".equals(tx.getType())) {
            throw new RuntimeException("Not a refund transaction");
        }

        if (!"PENDING".equals(tx.getStatus())) {
            throw new RuntimeException("This refund is already processed");
        }

        tx.setStatus("REFUNDED");
        repo.save(tx);
    }
}
