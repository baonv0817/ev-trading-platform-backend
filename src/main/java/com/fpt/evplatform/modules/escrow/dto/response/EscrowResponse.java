package com.fpt.evplatform.modules.escrow.dto.response;

import com.fpt.evplatform.common.enums.EscrowStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EscrowResponse {
    Integer escrowId;
    Integer dealId;
    BigDecimal ratePercent;
    BigDecimal feeAmount;
    EscrowStatus holdStatus;
    LocalDateTime createdAt;
    LocalDateTime releasedAt;
}
