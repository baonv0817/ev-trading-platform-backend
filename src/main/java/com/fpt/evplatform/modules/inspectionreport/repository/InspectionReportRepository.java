package com.fpt.evplatform.modules.inspectionreport.repository;


import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.modules.inspectionreport.entity.InspectionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, Integer> {
    Page<InspectionReport> findBySalePost_ListingId(Integer listingId, Pageable pageable);

    Optional<InspectionReport> findTopBySalePost_ListingIdOrderByCreatedAtDesc(Integer listingId);

    boolean existsBySalePost_ListingIdAndStatus(Integer listingId, InspectionReportStatus status);

    List<InspectionReport> findBySalePost_ListingIdOrderByCreatedAtDesc(Integer listingId);

    @Query(value = """
      SELECT ir.status
      FROM inspection_reports ir
      WHERE ir.listing_id = :listingId
      ORDER BY ir.created_at DESC
      LIMIT 1
      """, nativeQuery = true)
    String findLatestStatusByListingId(@org.springframework.data.repository.query.Param("listingId") Integer listingId);

    boolean existsBySalePost_ListingIdAndStatusAndResult(Integer listingId, InspectionReportStatus status, InspectionReportResult result);
}