package com.fpt.evplatform.modules.user.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserPlanResponse {
    private Integer planId;
    private String planName;
    private BigDecimal price;
    private String status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}