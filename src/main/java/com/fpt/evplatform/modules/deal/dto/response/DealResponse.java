package com.fpt.evplatform.modules.deal.dto.response;

import com.fpt.evplatform.common.enums.DealStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Double balanceDue;
    DealStatus status;
    LocalDateTime scheduledAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
