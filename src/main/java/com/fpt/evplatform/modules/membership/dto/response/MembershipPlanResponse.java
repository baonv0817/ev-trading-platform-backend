package com.fpt.evplatform.modules.membership.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPlanResponse {
    String name;
    String description;
    BigDecimal price;
    Integer durationDays;
    String currency;
    Integer maxPosts;
    Integer priorityLevel;
}
