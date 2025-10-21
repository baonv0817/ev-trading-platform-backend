package com.fpt.evplatform.modules.report.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReportResponse {
    Integer reporterId;
    String reporterName;
    Integer listingId;
    String listingDescription;
    String reason;
    String status;
}
