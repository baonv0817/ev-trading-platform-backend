package com.fpt.evplatform.modules.deal.entity;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "deals")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer dealId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    Offer offer;

    BigDecimal balanceDue;

    @Enumerated(EnumType.STRING)
    DealStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_site_id", nullable = true)
    PlatformSite platformSite;

    LocalDateTime scheduledAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = DealStatus.INITIALIZED;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
