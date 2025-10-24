package com.fpt.evplatform.modules.escrow.entity;

import com.fpt.evplatform.common.enums.EscrowStatus;
import com.fpt.evplatform.modules.deal.entity.Deal;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "escrows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Escrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer escrowId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    Deal deal;

    @Column(name = "rate_percent", precision = 5, scale = 2)
    BigDecimal ratePercent;

    @Column(name = "fee_amount", precision = 14, scale = 2)
    BigDecimal feeAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "hold_status", length = 20)
    EscrowStatus holdStatus;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "released_at")
    LocalDateTime releasedAt;

}

