package com.fpt.evplatform.modules.report.controller;

import com.fpt.evplatform.common.dto.ApiResponse;
import com.fpt.evplatform.common.enums.ReportStatus;
import com.fpt.evplatform.modules.report.dto.request.ReportRequest;
import com.fpt.evplatform.modules.report.dto.response.ReportResponse;
import com.fpt.evplatform.modules.report.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @PostMapping
    public ApiResponse<ReportResponse> createReport(@RequestBody ReportRequest req) {
        return ApiResponse.<ReportResponse>builder()
                .result(reportService.createReport(req))
                .message("Report submitted successfully")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<ReportResponse>> getReports(
            @RequestParam(required = false) ReportStatus status) {
        List<ReportResponse> result = (status == null)
                ? reportService.getAllReports()
                : reportService.getReportsByStatus(status);

        return ApiResponse.<List<ReportResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ReportResponse>> getReportsByUser(@PathVariable Integer userId) {
        return ApiResponse.<List<ReportResponse>>builder()
                .result(reportService.getReportsByUser(userId))
                .build();
    }

    @PutMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<Void> updateStatus(@RequestParam Integer reporterId, @RequestParam Integer listingId, @RequestParam ReportStatus status) {
        reportService.updateStatus(reporterId, listingId, status);
        return ApiResponse.<Void>builder()
                .message("Report status updated to " + status)
                .build();
    }
}
