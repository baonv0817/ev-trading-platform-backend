package com.fpt.evplatform.modules.inspectionreport.dto.request;

import com.fpt.evplatform.common.enums.ReportSourceType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class CreateReportRequest {
    Integer listingId;
    Integer inspectionOrderId; // optional
    ReportSourceType sourceType; // USER/SERVICE
    String provider;            // optional
    MultipartFile file;
}