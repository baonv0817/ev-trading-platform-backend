package com.fpt.evplatform.modules.payment.transaction.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    private Integer userId;
    private Integer referenceId;
    private String referenceType; // MEMBERSHIP / INSPECTION / DEAL
    private Long amount;
    private String type; // PAYMENT / REFUND
}