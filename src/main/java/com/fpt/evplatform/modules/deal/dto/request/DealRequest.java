package com.fpt.evplatform.modules.deal.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealRequest {
    Integer offerId;
    Integer platformSiteId;
    Double balanceDue;
    String scheduledAt;
}
