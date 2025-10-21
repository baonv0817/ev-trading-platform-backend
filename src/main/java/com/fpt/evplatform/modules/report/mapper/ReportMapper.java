package com.fpt.evplatform.modules.report.mapper;

import com.fpt.evplatform.modules.report.dto.request.ReportRequest;
import com.fpt.evplatform.modules.report.dto.response.ReportResponse;
import com.fpt.evplatform.modules.report.entity.Report;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReportMapper {

    @Mapping(
            target = "reportId",
            expression = "java(new com.fpt.evplatform.modules.report.entity.ReportId(reporter.getUserId(), listing.getListingId()))"
    )
    @Mapping(target = "reporter", source = "reporter")
    @Mapping(target = "listing", source = "listing")
    @Mapping(target = "reason", source = "req.reason")
    @Mapping(target = "status",
            expression = "java(com.fpt.evplatform.common.enums.ReportStatus.PENDING)")
    Report toReport(ReportRequest req, User reporter, SalePost listing);

    @Mapping(target = "reporterId", source = "reporter.userId")
    @Mapping(target = "reporterName", expression = "java(report.getReporter().getUsername())")
    @Mapping(target = "listingId", source = "listing.listingId")
    @Mapping(target = "listingDescription", source = "listing.description")
    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    ReportResponse toReportResponse(Report report);
}
