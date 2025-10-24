package com.fpt.evplatform.modules.offer.entity;

import com.fpt.evplatform.common.enums.OfferStatus;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "offers")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer offerId;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    SalePost listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    User buyer;

    Double proposedPrice;

    @Enumerated(EnumType.STRING)
    OfferStatus status;

    LocalDateTime createdAt;
    LocalDateTime expiresAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        expiresAt = LocalDateTime.now().plusDays(1);
    }
}
