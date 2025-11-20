package com.fpt.evplatform.modules.payment.transaction.controller;

import com.fpt.evplatform.modules.payment.transaction.entity.Transaction;
import com.fpt.evplatform.modules.payment.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final TransactionService transactionService;

    @GetMapping("/pending")
    public List<Transaction> getPendingRefunds() {
        return transactionService.getPendingRefunds();
    }

    @PostMapping("/confirm/{id}")
    public void confirmRefund(@PathVariable Integer id) {
        transactionService.markRefunded(id);
    }
}
