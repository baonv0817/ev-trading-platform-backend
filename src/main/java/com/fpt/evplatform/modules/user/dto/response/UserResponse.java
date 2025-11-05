package com.fpt.evplatform.modules.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.evplatform.modules.membership.dto.response.MembershipPlanResponse;
import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    Integer userId;
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
    BigDecimal wallet;
    Integer provinceCode;
    Integer districtCode;
    Integer wardCode;
    String status;
    String bio;
    String avatarUrl;
    String avatarPublicId;
    String avatarThumbUrl;
    MembershipPlanResponse plan;
    String planStatus;
    LocalDateTime startAt;
    LocalDateTime endAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
