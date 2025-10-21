package com.fpt.evplatform.modules.report.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReportId implements Serializable {
    private Integer reporterId;
    private Integer listingId;
}
