package com.fpt.evplatform.modules.inspectionreport.controller;

import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.ReportSourceType;
import com.fpt.evplatform.modules.inspectionreport.dto.request.CreateReportRequest;
import com.fpt.evplatform.modules.inspectionreport.dto.request.ReviewReportRequest;
import com.fpt.evplatform.modules.inspectionreport.dto.response.InspectionReportResponse;
import com.fpt.evplatform.modules.inspectionreport.service.InspectionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/inspection-reports")
@RequiredArgsConstructor
public class InspectionReportController {

    private final InspectionReportService reportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public InspectionReportResponse createReport(@ModelAttribute CreateReportRequest req) {
        return reportService.createWithPdf(req); // form có: listingId, inspectionOrderId?, sourceType, provider, file
    }


   @PostMapping("/{reportId}/review")
  public void review(@PathVariable Integer reportId,
                       @RequestBody ReviewReportRequest req) {
        InspectionReportResult res = req.getResult(); // PASS/FAIL/NEED_FIX
       reportService.review(reportId, req.isApprove(), res);
    }


   @GetMapping
    public Page<InspectionReportResponse> list(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return reportService.listAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }


   @GetMapping("/{reportId}")
    public InspectionReportResponse detail(@PathVariable Integer reportId) {
       return reportService.getDetail(reportId);
   }

   @GetMapping("/listing/{listingId}/verified")
   public boolean isVerified(@PathVariable Integer listingId) {
       return reportService.isListingVerified(listingId);
    }
}