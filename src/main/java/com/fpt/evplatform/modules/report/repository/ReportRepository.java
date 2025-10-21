package com.fpt.evplatform.modules.report.repository;

import com.fpt.evplatform.common.enums.ReportStatus;
import com.fpt.evplatform.modules.report.entity.Report;
import com.fpt.evplatform.modules.report.entity.ReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, ReportId> {
    List<Report> findByStatus(ReportStatus status);
    List<Report> findByReporter_UserId(Integer reporterId);
}
