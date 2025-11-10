package com.fpt.evplatform.modules.inspectionreport.dto.response;

import com.fpt.evplatform.common.enums.InspectionOrderStatus;
import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectionStatusResponse {

    // Latest order info (nullable)
    private Integer orderId;
    private InspectionOrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderCreatedAt;
    private LocalDateTime orderPaidAt;
    private Long orderAmount; // in cents/units as used in your project (or VND value)

    // Latest report info (nullable)
    private Integer reportId;
    private InspectionReportResult reportResult;
    private InspectionReportStatus reportStatus;
    private String reportUrl;
    private LocalDateTime reportCreatedAt;
    private LocalDateTime reportApprovedAt;

    // Convenience flags
    private boolean hasOrder;
    private boolean hasReport;
    private boolean paid;          // order đã thanh toán
    private boolean reportPending; // paid && no report OR report exists but PENDING_REVIEW
    private boolean verified;      // report exists and APPROVED
}