package com.fpt.evplatform.modules.deal.dto.response;

import com.fpt.evplatform.common.enums.DealStatus;
import com.fpt.evplatform.modules.user.dto.SimpleUserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealResponse {
    Integer dealId;
    Integer offerId;
    Integer platformSiteId;
    String platformSiteName;
    BigDecimal balanceDue;
    String status;
    LocalDateTime scheduledAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    SimpleUserDTO buyer;
    SimpleUserDTO seller;
    Integer listingId;
    BigDecimal feeAmount;
    BigDecimal sellerReceiveAmount;
}
