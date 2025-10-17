package com.fpt.evplatform.modules.membership.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipPlanUpdateRequest {
    String name;
    String description;
    BigDecimal price;
    Integer durationDays;
    String currency;
    Integer maxPosts;
    Integer priorityLevel;
}
