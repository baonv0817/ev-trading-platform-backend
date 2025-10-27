package com.fpt.evplatform.modules.inspectionreport.dto.request;

import com.fpt.evplatform.common.enums.InspectionReportResult;
import lombok.Data;

@Data
public class ReviewReportRequest {
    boolean approve;
    InspectionReportResult result; // PASS/FAIL/NEED_FIX
}