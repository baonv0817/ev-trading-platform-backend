package com.fpt.evplatform.modules.payment.transaction.controller;

import com.fpt.evplatform.modules.payment.transaction.dto.response.RevenueResponse;
import com.fpt.evplatform.modules.payment.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final TransactionService transactionService;

    @GetMapping
    public RevenueResponse getRevenue() {
        return transactionService.getRevenue();
    }
}