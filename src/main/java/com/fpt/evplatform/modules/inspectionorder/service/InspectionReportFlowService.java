package com.fpt.evplatform.modules.inspectionorder.service;


import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.ReportSourceType;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.inspectionreport.entity.InspectionReport;
import com.fpt.evplatform.modules.inspectionreport.repository.InspectionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InspectionReportFlowService {

    private final InspectionReportRepository reportRepo;

    /**
     * Luôn tạo report tương ứng với kết quả kiểm định.
     * PASS -> APPROVED
     * FAIL / NEED_FIX -> REJECTED
     */
    @Transactional
    public Integer createReportFromInspectionResult(
            InspectionOrder order,
            InspectionReportResult result
    ) {
        // xác định status theo kết quả
        InspectionReportStatus status;
        switch (result) {
            case PASS -> status = InspectionReportStatus.APPROVED;
            case FAIL -> status = InspectionReportStatus.REJECTED;
            default -> status = InspectionReportStatus.PENDING_REVIEW;
        }

        InspectionReport report = InspectionReport.builder()
                .salePost(order.getSalePost())
                .inspectionOrder(order)
                .sourceType(ReportSourceType.SERVICE)
                .provider("EV Verify Service")
                .result(result)
                .status(status)
                .approvedAt(result == InspectionReportResult.PASS ? LocalDateTime.now() : null)
                .createdAt(LocalDateTime.now())
                .build();

        return reportRepo.save(report).getReportId();
    }
}

