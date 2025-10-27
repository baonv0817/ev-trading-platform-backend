package com.fpt.evplatform.modules.inspectionorder.repository;

import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionOrderRepository extends JpaRepository<InspectionOrder, Integer> {

}