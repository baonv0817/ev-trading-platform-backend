package com.fpt.evplatform.modules.salepost.dto.response;

import lombok.Data;

@Data
public class BatteryPostResponse {
    private Long batteryId;
    private String chemistryName;
    private Double capacityKwh;
    private Integer sohPercent;
    private Integer cycleCount;
}
