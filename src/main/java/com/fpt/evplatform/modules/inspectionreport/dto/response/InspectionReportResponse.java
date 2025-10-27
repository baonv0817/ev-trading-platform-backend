package com.fpt.evplatform.modules.inspectionreport.dto.response;

import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.ReportSourceType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionReportResponse {
    Integer reportId;
    Integer listingId;
    Integer inspectionOrderId;
    ReportSourceType sourceType;
    String provider;
    InspectionReportStatus status;
    InspectionReportResult result;
    String reportUrl;
    LocalDateTime approvedAt;
    LocalDateTime createdAt;
}