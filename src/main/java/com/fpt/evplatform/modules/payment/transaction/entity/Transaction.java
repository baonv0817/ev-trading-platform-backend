package com.fpt.evplatform.modules.payment.transaction.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer referenceId; // membershipId, inspectionOrderId, dealId

    private String referenceType; // MEMBERSHIP / INSPECTION / DEAL

    private Long amount; // VND

    private String type; // PAYMENT / REFUND

    private String status; // SUCCEEDED / FAILED / PENDING

    private LocalDateTime createdAt = LocalDateTime.now();
}
