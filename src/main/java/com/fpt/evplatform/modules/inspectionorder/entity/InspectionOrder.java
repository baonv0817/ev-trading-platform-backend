package com.fpt.evplatform.modules.inspectionorder.entity;

import com.fpt.evplatform.common.enums.InspectionOrderStatus;
import com.fpt.evplatform.common.enums.PaymentStatus;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    SalePost salePost;

    LocalDateTime scheduledAt;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String street;

    @Enumerated(EnumType.STRING)
    InspectionOrderStatus status;

    LocalDateTime createdAt;
    BigDecimal amount;
    LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = InspectionOrderStatus.PENDING;
        if (paymentStatus == null) paymentStatus = PaymentStatus.UNPAID;
    }
}