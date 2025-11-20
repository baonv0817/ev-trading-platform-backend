package com.fpt.evplatform.modules.payment.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RevenueResponse {
    private Long totalPayment;
    private Long totalRefund;
    private Long netRevenue;
}