package com.fpt.evplatform.modules.report.entity;

import com.fpt.evplatform.common.enums.ReportStatus;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Report {
    @EmbeddedId
    ReportId reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("listingId")
    @JoinColumn(name = "listing_id")
    SalePost listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reporterId")
    @JoinColumn(name = "reporter_id")
    User reporter;

    @Column(name = "reason", length = 500)
    String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    ReportStatus status;
}
