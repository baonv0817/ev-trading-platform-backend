package com.fpt.evplatform.modules.offer.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OfferResponse {
    Integer offerId;
    Integer buyerId;
    String buyerName;
    Integer listingId;
    Double proposedPrice;
    String status;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
}
