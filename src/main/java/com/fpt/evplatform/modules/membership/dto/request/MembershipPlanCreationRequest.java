package com.fpt.evplatform.modules.membership.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPlanCreationRequest {
    String name;
    String description;
    BigDecimal price;
    Integer durationDays;
    String currency;
    Integer maxPosts;
    Integer priorityLevel;
}
