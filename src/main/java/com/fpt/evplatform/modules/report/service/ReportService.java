package com.fpt.evplatform.modules.report.service;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.enums.ReportStatus;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.report.dto.request.ReportRequest;
import com.fpt.evplatform.modules.report.dto.response.ReportResponse;
import com.fpt.evplatform.modules.report.entity.Report;
import com.fpt.evplatform.modules.report.entity.ReportId;
import com.fpt.evplatform.modules.report.mapper.ReportMapper;
import com.fpt.evplatform.modules.report.repository.ReportRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {

    ReportRepository reportRepository;
    ReportMapper reportMapper;
    UserRepository userRepository;
    SalePostRepository salePostRepository;

    public ReportResponse createReport(ReportRequest req) {
        User reporter = userRepository.findById(req.getReporterId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        SalePost listing = salePostRepository.findById(req.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));

        ReportId id = new ReportId(reporter.getUserId(), listing.getListingId());
        if (reportRepository.existsById(id)) {
            throw new AppException(ErrorCode.REPORT_ALREADY_EXISTS);
        }

        Report report = reportMapper.toReport(req, reporter, listing);
        reportRepository.save(report);
        return reportMapper.toReportResponse(report);
    }

    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    public List<ReportResponse> getReportsByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status).stream()
                .map(reportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    public List<ReportResponse> getReportsByUser(Integer userId) {
        return reportRepository.findByReporter_UserId(userId)
                .stream()
                .map(reportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    public void updateStatus(Integer reporterId, Integer listingId, ReportStatus status) {
        Report report = reportRepository.findById(new ReportId(reporterId, listingId))
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        report.setStatus(status);
        reportRepository.save(report);
    }

}
