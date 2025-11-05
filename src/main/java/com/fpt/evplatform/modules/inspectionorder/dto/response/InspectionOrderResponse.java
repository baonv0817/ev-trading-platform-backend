package com.fpt.evplatform.modules.inspectionorder.dto.response;

import com.fpt.evplatform.common.enums.InspectionOrderStatus;
import com.fpt.evplatform.common.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InspectionOrderResponse {
    private Integer orderId;
    private Integer listingId;
    private InspectionOrderStatus status;
    private PaymentStatus paymentStatus;

    private LocalDateTime scheduledAt;
    private Integer provinceCode;
    private Integer districtCode;
    private Integer wardCode;
    private String street;

    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}