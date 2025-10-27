package com.fpt.evplatform.modules.inspectionreport.repository;


import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.modules.inspectionreport.entity.InspectionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, Integer> {
    Page<InspectionReport> findBySalePost_ListingId(Integer listingId, Pageable pageable);

    boolean existsBySalePost_ListingIdAndStatusAndResult(Integer listingId, InspectionReportStatus status, InspectionReportResult result);
}