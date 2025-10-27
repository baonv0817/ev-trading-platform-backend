package com.fpt.evplatform.modules.inspectionorder.dto.request;

import com.fpt.evplatform.common.enums.InspectionReportResult;
import lombok.Data;

@Data
public class FinishInspectionRequest {
    InspectionReportResult result;
}