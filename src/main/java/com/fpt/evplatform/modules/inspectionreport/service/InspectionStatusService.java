package com.fpt.evplatform.modules.inspectionreport.service;

import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.PaymentStatus;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.inspectionorder.repository.InspectionOrderRepository;
import com.fpt.evplatform.modules.inspectionreport.dto.response.InspectionStatusResponse;
import com.fpt.evplatform.modules.inspectionreport.entity.InspectionReport;
import com.fpt.evplatform.modules.inspectionreport.repository.InspectionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InspectionStatusService {

    private final InspectionOrderRepository orderRepo;
    private final InspectionReportRepository reportRepo;

    public InspectionStatusResponse getStatusByListing(Integer listingId) {
        InspectionStatusResponse dto = new InspectionStatusResponse();

        Optional<InspectionOrder> maybeOrder = orderRepo.findTopBySalePost_ListingIdOrderByCreatedAtDesc(listingId);
        Optional<InspectionReport> maybeReport = reportRepo.findTopBySalePost_ListingIdOrderByCreatedAtDesc(listingId);

        // Fill order info if present
        if (maybeOrder.isPresent()) {
            InspectionOrder o = maybeOrder.get();
            dto.setHasOrder(true);
            dto.setOrderId(o.getOrderId());
            dto.setOrderStatus(o.getStatus());
            dto.setPaymentStatus(o.getPaymentStatus());
            dto.setOrderCreatedAt(o.getCreatedAt());
            dto.setOrderPaidAt(o.getPaidAt());
            dto.setOrderAmount(o.getAmount() == null ? null : o.getAmount().longValue());
            dto.setPaid(o.getPaymentStatus() != null && o.getPaymentStatus() == PaymentStatus.PAID);
        } else {
            dto.setHasOrder(false);
            dto.setPaid(false);
        }

        // Fill report info if present
        if (maybeReport.isPresent()) {
            InspectionReport r = maybeReport.get();
            dto.setHasReport(true);
            dto.setReportId(r.getReportId());
            dto.setReportResult(r.getResult());
            dto.setReportStatus(r.getStatus());
            dto.setReportUrl(r.getReportUrl());
            dto.setReportCreatedAt(r.getCreatedAt());
            dto.setReportApprovedAt(r.getApprovedAt());
            dto.setVerified(r.getStatus() != null && r.getStatus() == InspectionReportStatus.APPROVED);
        } else {
            dto.setHasReport(false);
            dto.setVerified(false);
        }

        // Compute reportPending:
        // - order exists & paid & no report -> pending
        // - OR report exists but status == PENDING_REVIEW -> pending
        boolean pending = false;
        if (dto.isHasOrder() && dto.isPaid() && !dto.isHasReport()) pending = true;
        if (dto.isHasReport() && dto.getReportStatus() != null && dto.getReportStatus() == InspectionReportStatus.PENDING_REVIEW)
            pending = true;
        dto.setReportPending(pending);

        return dto;
    }
}
