package com.fpt.evplatform.modules.inspectionreport.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.ReportStatus;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.inspectionorder.repository.InspectionOrderRepository;
import com.fpt.evplatform.modules.inspectionreport.dto.request.CreateReportRequest;
import com.fpt.evplatform.modules.inspectionreport.dto.response.InspectionReportResponse;
import com.fpt.evplatform.modules.inspectionreport.entity.InspectionReport;
import com.fpt.evplatform.modules.inspectionreport.repository.InspectionReportRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InspectionReportService {
    private final Cloudinary cloudinary;
    private final SalePostRepository salePostRepo;
    private final InspectionOrderRepository orderRepo;
    private final InspectionReportRepository reportRepo;

    @Transactional
    public InspectionReportResponse createWithPdf(CreateReportRequest req) {
        // 1. Kiểm tra listing và order
        SalePost sp = salePostRepo.findById(req.getListingId())
                .orElseThrow(() -> new IllegalArgumentException("SalePost not found"));

        InspectionOrder order = null;
        if (req.getInspectionOrderId() != null) {
            order = orderRepo.findById(req.getInspectionOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("InspectionOrder not found"));
        }

        MultipartFile file = req.getFile();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File PDF không được để trống");
        }

        // 2. Kiểm tra loại file
        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        boolean mimeOk = ct.equalsIgnoreCase("application/pdf");
        boolean extOk = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase().endsWith(".pdf");
        if (!(mimeOk || extOk)) {
            throw new IllegalArgumentException("Chỉ cho phép upload PDF.");
        }

        // 3. Tạo bản ghi trước để có reportId
        InspectionReport r = InspectionReport.builder()
                .salePost(sp)
                .inspectionOrder(order)
                .sourceType(req.getSourceType())
                .provider(req.getProvider())
                .status(InspectionReportStatus.PENDING_REVIEW)
                .build();
        reportRepo.save(r);

        // 4. Upload lên Cloudinary (resource_type = raw)
        String folder = "evplatform/inspections/" + r.getReportId();
        Map<String, Object> params = ObjectUtils.asMap(
                "folder", folder,
                "public_id", UUID.randomUUID().toString(),
                "resource_type", "raw",
                "type", "upload",
                "context", Map.of(
                        "listingId", String.valueOf(sp.getListingId()),
                        "reportId", String.valueOf(r.getReportId()),
                        "source", req.getSourceType().name()
                )
        );

        Map upload;
        try {
            upload = cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }

        String secureUrl = (String) upload.get("secure_url");
        String url       = (String) upload.get("url");

        // 5. Lưu URL vào bản ghi report
        r.setReportUrl(secureUrl != null ? secureUrl : url);
        reportRepo.save(r);

        // 6. Trả về response
        return toResponse(r);
    }

    @Transactional
    public void review(Integer reportId, boolean approve, InspectionReportResult result) {
        InspectionReport r = reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        r.setApprovedAt(LocalDateTime.now());
        r.setResult(result);
        r.setStatus(approve ? InspectionReportStatus.APPROVED : InspectionReportStatus.REJECTED);
        reportRepo.save(r);
    }

    @Transactional
    public Page<InspectionReportResponse> listByListing(Integer listingId, Pageable pageable) {
        return reportRepo.findBySalePost_ListingId(listingId, pageable).map(this::toResponse);
    }

    @Transactional
    public Page<InspectionReportResponse> listAll(Pageable pageable) {
        return reportRepo.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public InspectionReportResponse getDetail(Integer reportId) {
        InspectionReport r = reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        return toResponse(r);
    }

    @Transactional
    public boolean isListingVerified(Integer listingId) {
        return reportRepo.existsBySalePost_ListingIdAndStatusAndResult(
                listingId, InspectionReportStatus.APPROVED, InspectionReportResult.PASS
        );
    }

    private InspectionReportResponse toResponse(InspectionReport r) {
        InspectionReportResponse dto = new InspectionReportResponse();
        dto.setReportId(r.getReportId());
        dto.setListingId(r.getSalePost().getListingId());
        dto.setInspectionOrderId(r.getInspectionOrder() == null ? null : r.getInspectionOrder().getOrderId());
        dto.setSourceType(r.getSourceType());
        dto.setProvider(r.getProvider());
        dto.setStatus(r.getStatus());
        dto.setResult(r.getResult());
        dto.setReportUrl(r.getReportUrl());
        dto.setApprovedAt(r.getApprovedAt());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
