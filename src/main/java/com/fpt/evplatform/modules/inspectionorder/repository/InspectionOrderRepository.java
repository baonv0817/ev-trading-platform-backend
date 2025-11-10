package com.fpt.evplatform.modules.inspectionorder.repository;

import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InspectionOrderRepository extends JpaRepository<InspectionOrder, Integer> {
    // Lấy order mới nhất theo listingId
    Optional<InspectionOrder> findTopBySalePost_ListingIdOrderByCreatedAtDesc(Integer listingId);

    // Lấy toàn bộ order theo listingId (history)
    List<InspectionOrder> findBySalePost_ListingIdOrderByCreatedAtDesc(Integer listingId);
}