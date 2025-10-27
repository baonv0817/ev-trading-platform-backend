package com.fpt.evplatform.modules.inspectionreport.entity;

import com.fpt.evplatform.common.enums.InspectionReportResult;
import com.fpt.evplatform.common.enums.InspectionReportStatus;
import com.fpt.evplatform.common.enums.ReportSourceType;
import com.fpt.evplatform.modules.inspectionorder.entity.InspectionOrder;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    SalePost salePost;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_order_id")
    InspectionOrder inspectionOrder; // BIGINT trong ERD → ở đây dùng Integer/Long tùy DB

    @Enumerated(EnumType.STRING)
    ReportSourceType sourceType;

    String provider;

    @Enumerated(EnumType.STRING)
    InspectionReportResult result;

    @Enumerated(EnumType.STRING)
    InspectionReportStatus status;

    String reportUrl;

    @Column(name = "approved_at")
    LocalDateTime approvedAt;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = InspectionReportStatus.PENDING_REVIEW;
    }
}