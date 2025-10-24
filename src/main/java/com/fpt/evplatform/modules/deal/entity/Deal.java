package com.fpt.evplatform.modules.deal.entity;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.offer.entity.Offer;
import com.fpt.evplatform.modules.platformsite.entity.PlatformSite;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    Double balanceDue;

    @Enumerated(EnumType.STRING)
    DealStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_site_id")
    PlatformSite platformSite;

    LocalDateTime scheduledAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = DealStatus.PENDING;
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
